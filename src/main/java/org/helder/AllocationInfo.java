package org.helder;

public record AllocationInfo(
        int vmSize,
        int allocatableCPUs,
        int requestedCPUsPerVm,
        int wastedCPUs,
        int numberOfVms,
        double wasteRate) {
}