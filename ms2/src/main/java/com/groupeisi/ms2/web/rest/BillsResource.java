package com.groupeisi.ms2.web.rest;

import com.groupeisi.ms2.domain.Bills;
import com.groupeisi.ms2.domain.Customer;
import com.groupeisi.ms2.feign.CustomerRestClient;
import com.groupeisi.ms2.repository.BillsRepository;
import com.groupeisi.ms2.service.BillsService;
import com.groupeisi.ms2.service.dto.BillWithCustomerDTO;
import com.groupeisi.ms2.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.groupeisi.ms2.domain.Bills}.
 */
@RestController
@RequestMapping("/api/v1/bills")
public class BillsResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillsResource.class);

    private static final String ENTITY_NAME = "ms2Bills";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillsService billsService;

    private final BillsRepository billsRepository;

    private final CustomerRestClient customerRestClient;

    public BillsResource(BillsService billsService, BillsRepository billsRepository, CustomerRestClient customerRestClient) {
        this.billsService = billsService;
        this.billsRepository = billsRepository;
        this.customerRestClient = customerRestClient;
    }

    /**
     * {@code POST  /bills} : Create a new bills.
     *
     * @param bills the bills to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bills, or with status {@code 400 (Bad Request)} if the bills has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Bills> createBills(@RequestBody Bills bills) throws URISyntaxException {
        LOG.debug("REST request to save Bills : {}", bills);
        if (bills.getId() != null) {
            throw new BadRequestAlertException("A new bills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bills = billsService.save(bills);
        return ResponseEntity.created(new URI("/api/bills/" + bills.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bills.getId().toString()))
            .body(bills);
    }

    /**
     * {@code PUT  /bills/:id} : Updates an existing bills.
     *
     * @param id the id of the bills to save.
     * @param bills the bills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bills,
     * or with status {@code 400 (Bad Request)} if the bills is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bills> updateBills(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bills bills)
        throws URISyntaxException {
        LOG.debug("REST request to update Bills : {}, {}", id, bills);
        if (bills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bills = billsService.update(bills);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bills.getId().toString()))
            .body(bills);
    }

    /**
     * {@code PATCH  /bills/:id} : Partial updates given fields of an existing bills, field will ignore if it is null
     *
     * @param id the id of the bills to save.
     * @param bills the bills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bills,
     * or with status {@code 400 (Bad Request)} if the bills is not valid,
     * or with status {@code 404 (Not Found)} if the bills is not found,
     * or with status {@code 500 (Internal Server Error)} if the bills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bills> partialUpdateBills(@PathVariable(value = "id", required = false) final Long id, @RequestBody Bills bills)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Bills partially : {}, {}", id, bills);
        if (bills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bills> result = billsService.partialUpdate(bills);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bills.getId().toString())
        );
    }

    /**
     * {@code GET  /bills} : get all the bills.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bills in body.
     */
    @GetMapping("")
    public List<Bills> getAllBills() {
        LOG.debug("REST request to get all Bills");
        return billsService.findAll();
    }

    /**
     * {@code GET  /bills/:id} : get the "id" bills.
     *
     * @param id the id of the bills to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bills, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BillWithCustomerDTO> getBills(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bills : {}", id);

        Bills bills = billsService.findOne(id)
            .orElseThrow(() -> new EntityNotFoundException("Bill not found"));

        BillWithCustomerDTO responseDTO = new BillWithCustomerDTO();
        responseDTO.setId(bills.getId());
        responseDTO.setAmount(bills.getAmount());
        responseDTO.setBillDate(bills.getBillDate());

        try {
            Customer customer = customerRestClient.getCustomerById(bills.getCustomerId());
            LOG.debug("Retrieved customer for bill {}: {}", id, customer);

            responseDTO.setCustomerId(customer.getId());
            responseDTO.setCustomerName(customer.getName());
            responseDTO.setCustomerEmail(customer.getEmail());
            responseDTO.setCustomerPhone(customer.getPhone());
        } catch (Exception e) {
            LOG.error("Error retrieving customer for bill {}: {}", id, e.getMessage());
        }

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * {@code DELETE  /bills/:id} : delete the "id" bills.
     *
     * @param id the id of the bills to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBills(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bills : {}", id);
        billsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
