package hello.container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.yarn.annotation.OnContainerStart;
import org.springframework.yarn.annotation.YarnComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@YarnComponent
public class HelloPojo {

    private static final Log log = LogFactory.getLog(HelloPojo.class);

    @Autowired
    private Configuration configuration;


    @OnContainerStart
    public void shuffleSort() throws Exception {
        log.info("Hello from HelloPojo");

        List<Integer> sortedList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100_000_000; i++) {
            sortedList.add(random.nextInt());
        }

        List<Integer> randomList = new ArrayList<>();
        Collections.copy(randomList, sortedList);

        Collections.sort(sortedList);

        log.info("Trying to sort the list");
        int i = 0;
        while (!randomList.equals(sortedList) && i < 1_000_000) {
            Collections.shuffle(randomList);
            i++;
        }

        log.info("Printing the result of sorting");
        for (i = 0; i < 100; i++) {
            log.info(randomList.get(i));
        }
    }

}
