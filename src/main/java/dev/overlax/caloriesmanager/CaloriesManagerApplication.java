package dev.overlax.caloriesmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CaloriesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaloriesManagerApplication.class, args);
    }

}
