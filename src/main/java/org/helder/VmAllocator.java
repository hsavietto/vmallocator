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

        for (int vmSize : possibleVmSizes) {
            int jvmsPerVm = (vmSize - cpuOverheadPerVm) / cpuPerJVM;

            if (jvmsPerVm < 1) {
                continue;
            }

            double allocatableCPUs = vmSize - cpuOverheadPerVm;
            double requestedCPUsPerVm = (int) Math.floor(allocatableCPUs / cpuPerJVM) * cpuPerJVM;

            double minimumNumberOfVms = 1;
            int numberOfVms =  (int) Math.max(minimumNumberOfVms, 
                Math.ceil(
                    Math.ceil(
                        (requestedCPUsPerVm/allocatableCPUs) * (vmSize/requestedCPUsPerVm)
                    )
                ));// (int) Math.ceil((double)numberOfJVMs / jvmsPerVm);
            double wastedCPUs = (vmSize * numberOfVms) - (numberOfJVMs * cpuPerJVM);
            double wasteRate = 100.0 * (double)wastedCPUs / (double)(numberOfVms * vmSize);

            result.add(new AllocationInfo(vmSize, allocatableCPUs, requestedCPUsPerVm, wastedCPUs, numberOfVms, wasteRate));
        }

        result.sort((AllocationInfo info1, AllocationInfo info2) -> {
            // first, we prioritize fewer wasted CPUs (to reduce costs)
            double wasted = info1.wastedCPUs() - info2.wastedCPUs();

            if (wasted != 0) {
                return (int) wasted;
            }

            // second, we prioritize more VMs (for increased redundancy)
            int vms = info2.numberOfVms() - info1.numberOfVms();

            return vms;
        });

        return result;
    }
}
