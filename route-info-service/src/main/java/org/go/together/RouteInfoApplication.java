package org.go.together;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
@EnableKafka
@EnableTransactionManagement
@EnableEurekaClient
public class RouteInfoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RouteInfoApplication.class, args);
    }
}
