/*
 * Copyright 2024- Atomstate Technologies Private Limited.
 *
 * Licensed as a Atomstate Enterprise file under the Atomstate Enterprise
 * License (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://github.com/atomstatehq/atomstate/blob/main/license/ael.md
 */

package com.atomstate.atomstate.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class CpuBudgetTest {

    private static final int INITIAL_CPU_BUDGET = 10;
    private CpuBudget cpuBudget;

    @BeforeEach
    void setUp() {
        cpuBudget = new CpuBudget(INITIAL_CPU_BUDGET);
    }

    @Test
    void testConstructorInitializesSemaphoreAndCpuBudget() {
        assertNotNull(cpuBudget);
        assertThat(cpuBudget.getCpuBudget(), is(INITIAL_CPU_BUDGET));
        assertThat(cpuBudget.hasBudget(1), is(true)); // should have at least 1 permit
    }

    @Test
    void testTryAcquireWithValidCpus() {
        Optional<CpuPermit> permit = cpuBudget.tryAcquire(5);
        assertTrue(permit.isPresent());
        assertThat(permit.get().numCpus(), is(5));
        assertThat(cpuBudget.hasBudget(5), is(true));
    }

    @Test
    void testTryAcquireWithExcessiveCpus() {
        // Assuming the initial budget is less than 20
        Optional<CpuPermit> permit = cpuBudget.tryAcquire(20);
        assertFalse(permit.isPresent());
    }

    @Test
    void testTryAcquireWithNegativeCpus() {
        Optional<CpuPermit> permit = cpuBudget.tryAcquire(-5);
        assertFalse(permit.isPresent());
    }

    @Test
    void testTryAcquireWithZeroCpus() {
        Optional<CpuPermit> permit = cpuBudget.tryAcquire(0);
        assertFalse(permit.isPresent());
    }

    @Test
    void testHasBudgetReturnsTrueWhenSufficientBudget() {
        assertTrue(cpuBudget.hasBudget(5));
    }

    @Test
    void testHasBudgetReturnsFalseWhenInsufficientBudget() {
        cpuBudget.tryAcquire(10); // Acquire all permits
        assertFalse(cpuBudget.hasBudget(1)); // Should be false now
    }

    @Test
    void testGetNumCpusReturnsCorrectValue() {
        // Assuming the environment variable is not set, this should return the available processors
        int numCpus = CpuBudget.getNumCpus();
        assertThat(numCpus, is(greaterThan(0)));
    }

    @Test
    void testGetCpuBudgetWithPositiveParam() {
        int budget = CpuBudget.getCpuBudget(5);
        assertThat(budget, is(5));
    }

    @Test
    void testGetCpuBudgetWithNegativeParam() {
        int budget = CpuBudget.getCpuBudget(-5);
        assertThat(budget, is(greaterThan(0))); // Should return at least 1
    }

    @Test
    void testGetCpuBudgetWithZeroParam() {
        int budget = CpuBudget.getCpuBudget(0);
        assertThat(budget, is(greaterThan(0))); // Should allocate some CPUs
    }

    @Test
    void testDefaultCpuBudgetUnallocated() {
        int unallocated = CpuBudget.defaultCpuBudgetUnallocated(INITIAL_CPU_BUDGET);
        assertThat(unallocated, is(INITIAL_CPU_BUDGET / 4));
    }
}