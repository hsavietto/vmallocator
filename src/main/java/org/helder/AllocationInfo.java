package org.helder;

public record AllocationInfo(
        int vmSize,
        int wastedCPUs,
        int numberOfVms,
        double wasteRate) {
}