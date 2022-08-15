package org.helder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> vmSizes = new ArrayList<>(Arrays.asList(4, 8, 16, 32, 64, 128));
        int cpuPerJVM = 2;
        int numberOfJVMs = 1500;
        int cpuOverheadPerVm = 1;
        List<AllocationInfo> allocationAlternatives = VmAllocator.allocateVms(vmSizes, cpuPerJVM, numberOfJVMs, cpuOverheadPerVm);

        for (AllocationInfo info : allocationAlternatives) {
            System.out.println(
                    "VM CPUs = " + info.vmSize +
                    ", number of VMs required = " + info.numberOfVms +
                    ", wasted CPUs = " + info.wastedCPUs +
                    ", waste rate = " + info.wasteRate + "%");
        }
    }
}
