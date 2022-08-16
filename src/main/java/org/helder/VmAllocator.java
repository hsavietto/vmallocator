package org.helder;

import java.util.ArrayList;
import java.util.List;

public class VmAllocator {

    private List<Integer> possibleVmSizes;

    public VmAllocator(List<Integer> possibleVmSizes) {
        this.possibleVmSizes = new ArrayList<>(possibleVmSizes);
    }

    public List<AllocationInfo> allocateVms(int cpuPerJVM, int numberOfJVMs, int cpuOverheadPerVm) {
        var result = new ArrayList<AllocationInfo>();

        for (Integer vmSize : possibleVmSizes) {
            int jvmsPerVm = (vmSize - cpuOverheadPerVm) / cpuPerJVM;

            if (jvmsPerVm < 1) {
                continue;
            }

            int numberOfVms = (int)Math.ceil((double)numberOfJVMs / jvmsPerVm);
            int wastedCPUs = (vmSize * numberOfVms) - (numberOfJVMs * cpuPerJVM);
            double wasteRate = 100.0 * (double)wastedCPUs / (double)(numberOfVms * vmSize);

            result.add(new AllocationInfo(vmSize, wastedCPUs, numberOfVms, wasteRate));
        }

        result.sort((AllocationInfo info1, AllocationInfo info2) -> {
            // first, we prioritize fewer wasted CPUs (to reduce costs)
            int wasted = info1.wastedCPUs() - info2.wastedCPUs();

            if (wasted != 0) {
                return wasted;
            }

            // second, we prioritize more VMs (for increased redundancy)
            int vms = info2.numberOfVms() - info1.numberOfVms();

            return vms;
        });

        return result;
    }
}
