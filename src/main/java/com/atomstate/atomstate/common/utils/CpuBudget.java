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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.Semaphore;

/**
 * The {@code CpuBudget} class manages a budget for CPU permits using a semaphore.
 * This allows for tracking and controlling the allocation of CPU resources in a
 * concurrent environment, ensuring that a specified budget is adhered to.
 *
 * <p>Instances of this class can be used to acquire CPU permits while keeping
 * track of the remaining budget. The class logs actions and warnings related
 * to the CPU permit acquisition process.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     // Initialize a CpuBudget with a budget of 10 CPUs
 *     {@code CpuBudget cpuBudget = new CpuBudget(10);}
 *
 *     // Attempt to acquire 4 CPUs
 *     {@code Optional<CpuPermit> permit = cpuBudget.tryAcquire(4);}
 *     if (permit.isPresent()) {
 *         // Successfully acquired 4 CPUs
 *         {@code System.out.println("Acquired " + permit.get().numCpus() + " CPUs.");}
 *     } else {
 *         // Failed to acquire CPUs
 *         {@code System.out.println("Failed to acquire CPUs.");}
 *     }
 *
 *     // Check if budget allows acquiring 6 CPUs
 *     {@code boolean hasBudget = cpuBudget.hasBudget(6);}
 *     {@code System.out.println("Has budget for 6 CPUs: " + hasBudget);}
 * </pre>
 *
 * @author Atomstate
 * @since 1.0.0
 * @version 1.0.0
 */

public class CpuBudget {
    private static final Logger logger = LogManager.getLogger(CpuBudget.class);
    private final Semaphore semaphore;
    private final int cpuBudget;

    /**
     * Initializes a {@code CpuBudget} with a specified CPU budget.
     *
     * @param cpuBudget the total number of CPUs allocated for this budget
     * @author Atomstate
     * @since 1.0.0
     */
    public CpuBudget(int cpuBudget) {
        this.cpuBudget = cpuBudget;
        this.semaphore = new Semaphore(cpuBudget);
        logger.info("CpuBudget initialized with {} CPUs.", cpuBudget);
    }

    /**
     * Attempts to acquire a specified number of CPU permits.
     *
     * <p>If the desired number of CPUs is non-positive or exceeds the available
     * permits, an empty {@code Optional} is returned.</p>
     *
     * @param desiredCpus the number of CPUs requested for acquisition
     * @return an {@code Optional<CpuPermit>} containing a permit if successful,
     * or an empty {@code Optional} if the acquisition failed
     * @author Atomstate
     * @since 1.0.0
     */
    public Optional<CpuPermit> tryAcquire(int desiredCpus) {
        if (desiredCpus <= 0) {
            logger.warn("Attempted to acquire non-positive number of CPUs: {}", desiredCpus);
            return Optional.empty();
        }

        // Check if the requested CPUs exceed the available permits
        if (desiredCpus > semaphore.availablePermits()) {
            logger.warn("Requested CPUs exceed available permits: requested={}, available={}", desiredCpus, semaphore.availablePermits());
            return Optional.empty();
        }

        // Proceed to acquire the CPUs
        int numCpus = Math.min(semaphore.availablePermits(), desiredCpus);
        if (!semaphore.tryAcquire(numCpus)) {
            logger.warn("Failed to acquire CPUs: requested={}, available={}", desiredCpus, semaphore.availablePermits());
            return Optional.empty();
        }

        logger.info("Acquired {} CPUs.", numCpus);
        return Optional.of(new CpuPermit(numCpus));
    }

    /**
     * Checks if the specified number of CPUs can be acquired without exceeding
     * the available budget.
     *
     * @param desiredCpus the number of CPUs to check for budget availability
     * @return {@code true} if the budget allows the acquisition; {@code false} otherwise
     * @author Atomstate
     * @since 1.0.0
     */
    public boolean hasBudget(int desiredCpus) {
        return hasExactBudget(minPermits(desiredCpus));
    }

    /**
     * Checks if the available permits match or exceed the specified budget.
     *
     * @param budget the budget to check against available permits
     * @return {@code true} if available permits meet or exceed the budget;
     * {@code false} otherwise
     * @author Atomstate
     * @since 1.0.0
     */
    private boolean hasExactBudget(int budget) {
        return semaphore.availablePermits() >= budget;
    }

    /**
     * Calculates the minimum number of permits required based on the desired CPUs.
     *
     * @param desiredCpus the number of CPUs requested
     * @return the minimum permits needed, which is at least half of the desired CPUs or 1
     * @author Atomstate
     * @since 1.0.0
     */
    private int minPermits(int desiredCpus) {
        return Math.max(desiredCpus / 2, 1);
    }

    /**
     * Retrieves the number of CPUs from the environment variable or defaults to
     * the available processors if the variable is not set or invalid.
     *
     * @return the number of CPUs available to the application
     * @author Atomstate
     * @since 1.0.0
     */
    public static int getNumCpus() {
        String envVar = System.getenv("ATOMSTATE_NUM_CPUS");
        if (envVar != null) {
            try {
                int numCpus = Integer.parseInt(envVar);
                return Math.max(numCpus, Runtime.getRuntime().availableProcessors());
            } catch (NumberFormatException e) {
                logger.error("Invalid number of CPUs in environment variable: {}", envVar, e);
            }
        }
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Calculates the default amount of unallocated CPU budget, keeping 25% of
     * CPUs free.
     *
     * @param totalCpus the total number of CPUs available
     * @return the amount of CPU budget to keep unallocated
     * @author Atomstate
     * @since 1.0.0
     */
    public static int defaultCpuBudgetUnallocated(int totalCpus) {
        return totalCpus / 4;
    }

    /**
     * Determines the CPU budget based on the provided parameter and system
     * capabilities.
     *
     * @param cpuBudgetParam the parameter specifying the desired CPU budget
     * @return the final CPU budget for the application
     * @author Atomstate
     * @since 1.0.0
     */
    public static int getCpuBudget(int cpuBudgetParam) {
        int numCpus = getNumCpus();

        if (cpuBudgetParam < 0) {
            return Math.max(numCpus + cpuBudgetParam, 1);
        } else if (cpuBudgetParam == 0) {
            int unallocated = defaultCpuBudgetUnallocated(numCpus);
            return Math.max(numCpus - unallocated, 1);
        }

        return cpuBudgetParam;
    }

    /**
     * Retrieves the current CPU budget allocated for this instance.
     *
     * @return the total CPU budget for this instance
     * @author Atomstate
     * @since 1.0.0
     */
    public int getCpuBudget() {
        return cpuBudget;
    }
}