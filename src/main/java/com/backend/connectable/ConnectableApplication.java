package com.backend.connectable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ConnectableApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectableApplication.class, args);
    }

}
