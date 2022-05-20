package uz.pdp.attachments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AttachmentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttachmentsApplication.class, args);
    }

}
