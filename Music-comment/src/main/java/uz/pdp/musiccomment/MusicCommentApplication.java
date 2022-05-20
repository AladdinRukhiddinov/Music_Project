package uz.pdp.musiccomment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "uz.pdp.feignclients")
public class MusicCommentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicCommentApplication.class, args);
    }

}
