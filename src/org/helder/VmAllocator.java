package org.helder;

import java.util.ArrayList;
import java.util.List;

public class VmAllocator {

    public static List<AllocationInfo> allocateVms(
            List<Integer> possibleVmSizes, int cpuPerJVM, int numberOfJVMs, int cpuOverheadPerVm) {
        List<AllocationInfo> result = new ArrayList<>();

        for (Integer vmSize : possibleVmSizes) {
            AllocationInfo info = new AllocationInfo();
            info.vmSize = vmSize;
            int jvmsPerVm = (vmSize - cpuOverheadPerVm) / cpuPerJVM;

            if (jvmsPerVm < 1) {
                continue;
            }

            info.numberOfVms = (int)Math.ceil((double)numberOfJVMs / jvmsPerVm);
            info.wastedCPUs = (vmSize * info.numberOfVms) - (numberOfJVMs * cpuPerJVM);
            info.wasteRate = 100.0 * (double)info.wastedCPUs / (double)(info.numberOfVms * vmSize);
            result.add(info);
        }

        result.sort((AllocationInfo info1, AllocationInfo info2) -> {
            // first, we prioritize fewer wasted CPUs (to reduce costs)
            int wasted = info1.wastedCPUs - info2.wastedCPUs;

            if (wasted != 0) {
                return wasted;
            }

            // second, we prioritize more VMs (for increased redundancy)
            int vms = info2.numberOfVms - info1.numberOfVms;
            return vms;
        });
        return result;
    }
}
