package org.helder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        // Inputs:
        var vmSizes = Arrays.asList(4, 8, 16, 32, 48, 64);
        int cpuPerJVM = 2;
        int numberOfJVMs = 16;
        int minimumVMCount = 1;
        int cpuOverheadPerVm = 1;

        System.out.println("Number of JVMs: " + numberOfJVMs);
        System.out.println("CPUs per JVMs: " + cpuPerJVM);
        System.out.println("\nAllocation analysis:");

        var vmAllocator = new VmAllocator(cpuPerJVM, numberOfJVMs, cpuOverheadPerVm, minimumVMCount);

        for (int vmSize : vmSizes) {
            var info = vmAllocator.allocate(vmSize);
            System.out.println(info);
        }

        // Find best allocation by waste rate
        System.out.println("\nBest allocation by waste rate:");
        vmSizes.stream().map(vmAllocator::allocate).sorted(vmAllocator.getComparatorByWasteRate()).findFirst().ifPresent(System.out::println);

        // Find best allocation by redundancy
        System.out.println("\nBest allocation by redundancy:");
        vmSizes.stream().map(vmAllocator::allocate).sorted(vmAllocator.getComparatorByRedundancy()).findFirst().ifPresent(System.out::println);

        /* 
        for (AllocationInfo info : allocationAlternatives) {
            System.out.println(
                    "Number of JVMs: " + numberOfJVMs +
                    ", VM CPUs = " + info.vmSize() +
                    ", # of VMs required = " + info.numberOfVms() +
                    ", wasted CPUs = " + info.wastedCPUs() +
                    ", waste rate = " + info.wasteRate() + "%");
        }
        */
    }

}
