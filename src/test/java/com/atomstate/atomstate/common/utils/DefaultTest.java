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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultTest {
    @Test
    public void testDefaultCpuBudgetUnallocated() {
        assertEquals(0, Default.defaultCpuBudgetUnallocated(0));
        assertEquals(0, Default.defaultCpuBudgetUnallocated(1));
        assertEquals(0, Default.defaultCpuBudgetUnallocated(2));
        assertEquals(-1, Default.defaultCpuBudgetUnallocated(3));
        assertEquals(-1, Default.defaultCpuBudgetUnallocated(32));
        assertEquals(-2, Default.defaultCpuBudgetUnallocated(33));
        assertEquals(-2, Default.defaultCpuBudgetUnallocated(48));
        assertEquals(-3, Default.defaultCpuBudgetUnallocated(49));
        assertEquals(-3, Default.defaultCpuBudgetUnallocated(64));
        assertEquals(-4, Default.defaultCpuBudgetUnallocated(65));
        assertEquals(-4, Default.defaultCpuBudgetUnallocated(96));
        assertEquals(-6, Default.defaultCpuBudgetUnallocated(97));
        assertEquals(-6, Default.defaultCpuBudgetUnallocated(128));
        assertEquals(-8, Default.defaultCpuBudgetUnallocated(130));
    }

    @Test
    public void testThreadCountForHnsw() {
        assertEquals(1, Default.threadCountForHnsw(1));
        assertEquals(8, Default.threadCountForHnsw(8));
        assertEquals(8, Default.threadCountForHnsw(32));
        assertEquals(12, Default.threadCountForHnsw(50));
        assertEquals(12, Default.threadCountForHnsw(64));
        assertEquals(16, Default.threadCountForHnsw(65));
        assertEquals(16, Default.threadCountForHnsw(80));
        assertEquals(16, Default.threadCountForHnsw(100));
    }
}