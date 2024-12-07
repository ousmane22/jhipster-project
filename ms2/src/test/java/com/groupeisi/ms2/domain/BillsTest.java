package com.groupeisi.ms2.domain;

import static com.groupeisi.ms2.domain.BillsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.groupeisi.ms2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bills.class);
        Bills bills1 = getBillsSample1();
        Bills bills2 = new Bills();
        assertThat(bills1).isNotEqualTo(bills2);

        bills2.setId(bills1.getId());
        assertThat(bills1).isEqualTo(bills2);

        bills2 = getBillsSample2();
        assertThat(bills1).isNotEqualTo(bills2);
    }
}
