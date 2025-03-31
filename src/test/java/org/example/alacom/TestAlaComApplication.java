package org.example.alacom;

import org.springframework.boot.SpringApplication;

public class TestAlaComApplication {

    public static void main(String[] args) {
        SpringApplication.from(AlaComApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
