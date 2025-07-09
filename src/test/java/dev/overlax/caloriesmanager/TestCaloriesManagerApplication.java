package dev.overlax.caloriesmanager;

import org.springframework.boot.SpringApplication;

public class TestCaloriesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.from(CaloriesManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
