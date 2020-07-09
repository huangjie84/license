package com.orcadt.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class LicenseClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseClientApplication.class, args);
    }

}
