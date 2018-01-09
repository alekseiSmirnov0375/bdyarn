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

import java.util.HashMap;
import java.util.Map;

public class SleepingApplicationMaster extends StaticAppmaster {

    private static final Log log = LogFactory.getLog(SleepingApplicationMaster.class);

    @Override
    public ContainerLaunchContext preLaunch(Container container, ContainerLaunchContext context) {
        Map<String, String> env = context.getEnvironment();
        for (Map.Entry<String, String> entry :env.entrySet()) {
            log.info("Key: " + entry.getKey() + ", value: " + entry.getValue());
        }
        while(true) {
            try {
                log.info("Hey!");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.info("Oops!");
                return context;
            }
        }
    }
}
