package com.groupeisi.ms2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class BillsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Bills getBillsSample1() {
        return new Bills().id(1L).customerId(1L);
    }

    public static Bills getBillsSample2() {
        return new Bills().id(2L).customerId(2L);
    }

    public static Bills getBillsRandomSampleGenerator() {
        return new Bills().id(longCount.incrementAndGet()).customerId(longCount.incrementAndGet());
    }
}
