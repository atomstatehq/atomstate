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

import java.time.Duration;

import static com.atomstate.atomstate.common.utils.CpuBudget.getNumCpus;

/**
 * The Default class provides configuration constants and utility methods
 * related to CPU usage and consensus operations.
 *
 * @author Atomstate
 * @version 1.0.0
 * @since 1.0.0
 */
public class Default {
    /**
     * The version string for the Atomstate.
     *
     * @since 1.0.0
     */
    public static final String ATOMSTATE_VERSION_STRING = "1.0.0";

    /**
     * The parsed version object for Atomstate, based on the version string.
     *
     * @since 1.0.0
     */
    public static final Version ATOMSTATE_VERSION = Version.parse(ATOMSTATE_VERSION_STRING);

    /**
     * The number of retries for confirming a consensus operation.
     *
     * @since 1.0.0
     */
    public static final int CONSENSUS_CONFIRM_RETRIES = 3;

    /**
     * The default timeout for consensus meta operations.
     *
     * @since 1.0.0
     */
    public static final Duration CONSENSUS_META_OP_WAIT = Duration.ofSeconds(10);

    /**
     * The maximum number of pooled elements to preserve in memory.
     *
     * @since 1.0.0
     */
    public static final int POOL_KEEP_LIMIT = Math.min(Math.max(getNumCpus(), 16), 128);

    /**
     * Returns the default CPU budget parameter based on the number of CPUs.
     * <p>
     * The budget is determined as follows:
     * <ul>
     *   <li>0 for 0, 1, or 2 CPUs</li>
     *   <li>-1 for 3 to 32 CPUs</li>
     *   <li>-2 for 33 to 48 CPUs</li>
     *   <li>-3 for 49 to 64 CPUs</li>
     *   <li>-4 for 65 to 96 CPUs</li>
     *   <li>-6 for 97 to 128 CPUs</li>
     *   <li>A negative value calculated as -(numCpu / 16) for more than 128 CPUs</li>
     * </ul>
     * </p>
     *
     * @param numCpu the number of CPUs
     * @return the default CPU budget parameter
     *
     * <p>Example usage:</p>
     * <pre>
     *     // Get the default CPU budget for a specific CPU count
     *     {@code int budget1 = Default.defaultCpuBudgetUnallocated(16);} // returns -1
     *     {@code int budget2 = Default.defaultCpuBudgetUnallocated(50);} // returns -3
     *     {@code int budget3 = Default.defaultCpuBudgetUnallocated(130);} // returns -8
     *
     *     // Output the results
     *     {@code System.out.println("Budget for 16 CPUs: " + budget1);}
     *     {@code System.out.println("Budget for 50 CPUs: " + budget2);}
     *     {@code System.out.println("Budget for 130 CPUs: " + budget3);}
     * </pre>
     * @author Atomstate
     * @since 1.0.0
     */
    public static int defaultCpuBudgetUnallocated(int numCpu) {
        if (numCpu <= 2) return 0;
        if (numCpu <= 32) return -1;
        if (numCpu <= 48) return -2;
        if (numCpu <= 64) return -3;
        if (numCpu <= 96) return -4;
        if (numCpu <= 128) return -6;
        return -(numCpu / 16);
    }

    /**
     * Determines the default number of threads to be used for HNSW graph building
     * and optimization tasks based on the number of CPUs available.
     * <p>
     * HNSW (Hierarchical Navigable Small Worlds) graph building is a sophisticated method used
     * primarily for efficient approximate nearest neighbor (ANN) searches in high-dimensional spaces.
     *
     * <p>
     * The number of threads is determined as follows:
     * <ul>
     *   <li>At least 1 thread and at most 8 threads for up to 48 CPUs</li>
     *   <li>12 threads for 49 to 64 CPUs</li>
     *   <li>16 threads for more than 64 CPUs</li>
     * </ul>
     *
     * @param numCpu the number of CPUs available
     * @return the number of threads to use for HNSW tasks
     *
     * <p>Example usage:</p>
     * <pre>
     *     // Get the number of threads for a specific CPU count
     *     {@code int threads1 = Default.threadCountForHnsw(32);} // returns 8
     *     {@code int threads2 = Default.threadCountForHnsw(50);} // returns 12
     *     {@code int threads3 = Default.threadCountForHnsw(80);} // returns 16
     *
     *     // Output the results
     *     {@code System.out.println("Threads for 32 CPUs: " + threads1);}
     *     {@code System.out.println("Threads for 50 CPUs: " + threads2);}
     *     {@code System.out.println("Threads for 80 CPUs: " + threads3);}
     * </pre>
     * @author Atomstate
     * @since 1.0.0
     */
    public static int threadCountForHnsw(int numCpu) {
        if (numCpu <= 48) {
            return Math.max(1, Math.min(numCpu, 8));
        } else if (numCpu <= 64) {
            return 12;
        } else {
            return 16;
        }
    }
}