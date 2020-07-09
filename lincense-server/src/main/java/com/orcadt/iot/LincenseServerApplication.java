package com.orcadt.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class LincenseServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LincenseServerApplication.class, args);
    }

}
