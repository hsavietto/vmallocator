package org.helder;

public record AllocationInfo(
        int vmSize,
        double allocatableCPUs,
        double requestedCPUsPerVm,
        double wastedCPUs,
        int numberOfVms,
        double wasteRate) {
}