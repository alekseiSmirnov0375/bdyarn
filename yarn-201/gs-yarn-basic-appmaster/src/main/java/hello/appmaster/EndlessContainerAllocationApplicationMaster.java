package hello.appmaster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerStatus;
import org.apache.hadoop.yarn.api.records.FinalApplicationStatus;
import org.springframework.yarn.am.StaticEventingAppmaster;
import org.springframework.yarn.am.allocate.AbstractAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.am.monitor.ContainerAware;

import java.util.Arrays;

public class EndlessContainerAllocationApplicationMaster extends StaticEventingAppmaster {

    private static final Log log = LogFactory.getLog(EndlessContainerAllocationApplicationMaster.class);
    private int startNum = 1;

    @Override
    public void submitApplication() {
        log.info("Submitting application");
        registerAppmaster();
        start();
        if(getAllocator() instanceof AbstractAllocator) {
            ((AbstractAllocator)getAllocator()).setApplicationAttemptId(getApplicationAttemptId());
        }
    }

    @Override
    protected void onContainerAllocated(Container container) {
        if (getMonitor() instanceof ContainerAware) {
            ((ContainerAware)getMonitor()).onContainer(Arrays.asList(container));
        }
        getLauncher().launchContainer(container, getCommands());
    }

    public void startContainer(YarnAppParameters params) {
        String allocationGroupId = "yarn-test-app" + startNum;
        if (getAllocator() instanceof DefaultContainerAllocator) {
            DefaultContainerAllocator allocator = (DefaultContainerAllocator)getAllocator();
            allocator.setAllocationValues(
                    allocationGroupId,
                    params.getPriority(),
                    "",
                    params.getCoresNum(),
                    params.getMemory(),
                    false
            );
            ContainerAllocateData data = new ContainerAllocateData();
            data.setId(allocationGroupId);
            data.addAny(params.getContainersNum());
            allocator.allocateContainers(data);
            log.info("Container allocation: " + params.getContainersNum() + " allocated containers");
            startNum++;
        }
    }

    @Override
    protected void onContainerCompleted(ContainerStatus status) {

        log.info("onContainerCompleted:" + status);

        if (getMonitor() instanceof ContainerAware) {
            ((ContainerAware)getMonitor()).onContainerStatus(Arrays.asList(status));
        }

        int exitStatus = status.getExitStatus();

        // 0 - ok, -100 - container released by app
        if (exitStatus == 0 || exitStatus == -100) {
            if (isComplete()) {
                log.info("No more active containers. Waiting for allocation");
            }
        } else {
            log.warn("Got ContainerStatus=[" + status + "]");
            if (!onContainerFailed(status)) {
                setFinalApplicationStatus(FinalApplicationStatus.FAILED);
                notifyCompleted();
            }
        }
    }

    public void stopApplicationMaster() {
        setFinalApplicationStatus(FinalApplicationStatus.SUCCEEDED);
        notifyCompleted();
    }
}
