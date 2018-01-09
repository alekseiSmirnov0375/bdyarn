package hello.appmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class AppmasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppmasterApplication.class, args);
    }

}
