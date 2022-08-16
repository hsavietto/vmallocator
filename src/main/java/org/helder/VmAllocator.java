package org.helder;

import java.util.Comparator;
import java.util.List;

public class VmAllocator {

    private int minimumVMCount;
    private int cpuPerJVM;
    private int cpuOverheadPerVm;
    private int numberOfJVMs;

    public VmAllocator(int cpuPerJVM, int numberOfJVMs, int cpuOverheadPerVm, int minimumVMCount) {
        this.minimumVMCount = minimumVMCount;
        this.cpuPerJVM = cpuPerJVM;
        this.numberOfJVMs = numberOfJVMs;
        this.cpuOverheadPerVm = cpuOverheadPerVm;
        this.minimumVMCount = minimumVMCount;
    }

    public AllocationInfo allocate(int vmSize) {
        int allocatableCPUs = vmSize - cpuOverheadPerVm;
        int requestedCPUsPerVm = (int) Math.floor((double) allocatableCPUs / cpuPerJVM) * cpuPerJVM;
        int totalCPUs = numberOfJVMs * cpuPerJVM;

        int numberOfVms = (int) Math.max(minimumVMCount,
                Math.ceil(
                        Math.ceil((double) requestedCPUsPerVm / allocatableCPUs) * ((double) totalCPUs / requestedCPUsPerVm)));
        int billableCPUs = numberOfVms * vmSize;
        int idleCPUs = billableCPUs - totalCPUs;
        double wasteRate = 100.0 * (double) idleCPUs / (numberOfVms * vmSize);

        return new AllocationInfo(vmSize, allocatableCPUs, requestedCPUsPerVm, idleCPUs, numberOfVms, wasteRate);
    }

    public AllocationInfo findBestAllocationByRedundancy(List<AllocationInfo> infos) {
        return infos.stream().sorted(getComparatorByRedundancy()).findFirst().orElse(null);
    }

    public AllocationInfo findBestAllocationByWasteRate(List<AllocationInfo> infos) {
        return infos.stream().sorted(getComparatorByWasteRate()).findFirst().orElse(null);
    }

    public Comparator<AllocationInfo> getComparatorByWasteRate() {
        return (AllocationInfo info1, AllocationInfo info2) -> {
            // first, we prioritize fewer wasted CPUs (to reduce costs)
            double wasted = info1.wastedCPUs() - info2.wastedCPUs();

            return (int) wasted;
        };
    }

    public Comparator<AllocationInfo> getComparatorByRedundancy() {
        return (AllocationInfo info1, AllocationInfo info2) -> {
            // second, we prioritize more VMs (for increased redundancy)
            int vms = info2.numberOfVms() - info1.numberOfVms();

            return vms;
        };
    }

}
