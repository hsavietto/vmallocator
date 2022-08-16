package org.helder;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        var vmSizes = Arrays.asList(4, 8, 16, 32, 48, 64);
        int cpuPerJVM = 2;
        int numberOfJVMs = 16;
        int cpuOverheadPerVm = 1;

        var vmAllocator = new VmAllocator(vmSizes);

        var allocationAlternatives = vmAllocator.allocateVms(cpuPerJVM, numberOfJVMs, cpuOverheadPerVm);

        for (AllocationInfo info : allocationAlternatives) {
            System.out.println(
                    "Number of JVMs: " + numberOfJVMs +
                    ", VM CPUs = " + info.vmSize() +
                    ", # of VMs required = " + info.numberOfVms() +
                    ", wasted CPUs = " + info.wastedCPUs() +
                    ", waste rate = " + info.wasteRate() + "%");
        }
    }

}
