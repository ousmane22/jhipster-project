package com.groupeisi.ms2.web.rest;

import static com.groupeisi.ms2.domain.BillsAsserts.*;
import static com.groupeisi.ms2.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupeisi.ms2.IntegrationTest;
import com.groupeisi.ms2.domain.Bills;
import com.groupeisi.ms2.repository.BillsRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BillsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BillsResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_BILL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BILL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/bills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BillsRepository billsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBillsMockMvc;

    private Bills bills;

    private Bills insertedBills;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bills createEntity() {
        return new Bills().amount(DEFAULT_AMOUNT).billDate(DEFAULT_BILL_DATE).customerId(DEFAULT_CUSTOMER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bills createUpdatedEntity() {
        return new Bills().amount(UPDATED_AMOUNT).billDate(UPDATED_BILL_DATE).customerId(UPDATED_CUSTOMER_ID);
    }

    @BeforeEach
    public void initTest() {
        bills = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBills != null) {
            billsRepository.delete(insertedBills);
            insertedBills = null;
        }
    }

    @Test
    @Transactional
    void createBills() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bills
        var returnedBills = om.readValue(
            restBillsMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bills)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Bills.class
        );

        // Validate the Bills in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBillsUpdatableFieldsEquals(returnedBills, getPersistedBills(returnedBills));

        insertedBills = returnedBills;
    }

    @Test
    @Transactional
    void createBillsWithExistingId() throws Exception {
        // Create the Bills with an existing ID
        bills.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillsMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bills)))
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBills() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        // Get all the billsList
        restBillsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bills.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].billDate").value(hasItem(DEFAULT_BILL_DATE.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())));
    }

    @Test
    @Transactional
    void getBills() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        // Get the bills
        restBillsMockMvc
            .perform(get(ENTITY_API_URL_ID, bills.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bills.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.billDate").value(DEFAULT_BILL_DATE.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingBills() throws Exception {
        // Get the bills
        restBillsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBills() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bills
        Bills updatedBills = billsRepository.findById(bills.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBills are not directly saved in db
        em.detach(updatedBills);
        updatedBills.amount(UPDATED_AMOUNT).billDate(UPDATED_BILL_DATE).customerId(UPDATED_CUSTOMER_ID);

        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBills.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBills))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBillsToMatchAllProperties(updatedBills);
    }

    @Test
    @Transactional
    void putNonExistingBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bills.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bills))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bills))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBillsWithPatch() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bills using partial update
        Bills partialUpdatedBills = new Bills();
        partialUpdatedBills.setId(bills.getId());

        partialUpdatedBills.billDate(UPDATED_BILL_DATE).customerId(UPDATED_CUSTOMER_ID);

        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBills.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBills))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBills, bills), getPersistedBills(bills));
    }

    @Test
    @Transactional
    void fullUpdateBillsWithPatch() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bills using partial update
        Bills partialUpdatedBills = new Bills();
        partialUpdatedBills.setId(bills.getId());

        partialUpdatedBills.amount(UPDATED_AMOUNT).billDate(UPDATED_BILL_DATE).customerId(UPDATED_CUSTOMER_ID);

        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBills.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBills))
            )
            .andExpect(status().isOk());

        // Validate the Bills in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBillsUpdatableFieldsEquals(partialUpdatedBills, getPersistedBills(partialUpdatedBills));
    }

    @Test
    @Transactional
    void patchNonExistingBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bills.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bills))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bills))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBills() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bills.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBillsMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bills)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bills in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBills() throws Exception {
        // Initialize the database
        insertedBills = billsRepository.saveAndFlush(bills);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bills
        restBillsMockMvc
            .perform(delete(ENTITY_API_URL_ID, bills.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return billsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Bills getPersistedBills(Bills bills) {
        return billsRepository.findById(bills.getId()).orElseThrow();
    }

    protected void assertPersistedBillsToMatchAllProperties(Bills expectedBills) {
        assertBillsAllPropertiesEquals(expectedBills, getPersistedBills(expectedBills));
    }

    protected void assertPersistedBillsToMatchUpdatableProperties(Bills expectedBills) {
        assertBillsAllUpdatablePropertiesEquals(expectedBills, getPersistedBills(expectedBills));
    }
}
