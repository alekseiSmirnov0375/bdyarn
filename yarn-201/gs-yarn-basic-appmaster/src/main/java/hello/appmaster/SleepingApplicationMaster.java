package hello.appmaster;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerLaunchContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.yarn.am.StaticAppmaster;
import org.springframework.yarn.am.StaticEventingAppmaster;
import org.springframework.yarn.am.allocate.AbstractAllocator;
import org.springframework.yarn.am.allocate.ContainerAllocateData;
import org.springframework.yarn.am.allocate.DefaultContainerAllocator;
import org.springframework.yarn.am.monitor.ContainerAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SleepingApplicationMaster extends StaticEventingAppmaster {

    private static final Log log = LogFactory.getLog(SleepingApplicationMaster.class);

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
//        if (getAllocator() instanceof DefaultContainerAllocator) {
//            DefaultContainerAllocator allocator = (DefaultContainerAllocator)getAllocator();
//            allocator.setAllocationValues(
//                    "yarn-test-app",
//                    params.getPriority(),
//                    "",
//                    params.getCoresNum(),
//                    params.getMemory(),
//                    false
//            );
//            ContainerAllocateData data = new ContainerAllocateData();
//            data.setId("yarn-test-app");
//            data.addAny(params.getContainersNum());
//            allocator.allocateContainers(data);
//        }
        if (getAllocator() instanceof DefaultContainerAllocator) {
            DefaultContainerAllocator allocator = (DefaultContainerAllocator)getAllocator();
            allocator.setPriority(params.getPriority());
            allocator.setMemory(params.getMemory());
            allocator.setVirtualcores(params.getCoresNum());
            getAllocator().allocateContainers(params.getContainersNum());
            log.info("Container allocation: " + params.getContainersNum() + " allocated containers");
        }
    }
}
