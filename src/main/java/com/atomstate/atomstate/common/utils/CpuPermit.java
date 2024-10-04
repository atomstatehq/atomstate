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

/**
 * Represents a permit for CPU usage, encapsulating the number of CPUs
 * allocated through the {@code CpuBudget} management system.
 *
 * <p>The {@code CpuPermit} record is a simple data structure used to
 * represent the number of CPUs that have been successfully acquired
 * for a particular operation. This allows for type-safe handling of
 * CPU permits in concurrent environments.</p>
 *
 * @param numCpus the number of CPUs granted by this permit
 *
 * @author Atomstate
 * @version 1.0.0
 * @since 1.0.0
 */
public record CpuPermit(int numCpus) {
}