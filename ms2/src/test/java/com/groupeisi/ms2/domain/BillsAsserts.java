package com.groupeisi.ms2.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class BillsAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillsAllPropertiesEquals(Bills expected, Bills actual) {
        assertBillsAutoGeneratedPropertiesEquals(expected, actual);
        assertBillsAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillsAllUpdatablePropertiesEquals(Bills expected, Bills actual) {
        assertBillsUpdatableFieldsEquals(expected, actual);
        assertBillsUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillsAutoGeneratedPropertiesEquals(Bills expected, Bills actual) {
        assertThat(expected)
            .as("Verify Bills auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillsUpdatableFieldsEquals(Bills expected, Bills actual) {
        assertThat(expected)
            .as("Verify Bills relevant properties")
            .satisfies(e -> assertThat(e.getAmount()).as("check amount").isEqualTo(actual.getAmount()))
            .satisfies(e -> assertThat(e.getBillDate()).as("check billDate").isEqualTo(actual.getBillDate()))
            .satisfies(e -> assertThat(e.getCustomerId()).as("check customerId").isEqualTo(actual.getCustomerId()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBillsUpdatableRelationshipsEquals(Bills expected, Bills actual) {
        // empty method
    }
}
