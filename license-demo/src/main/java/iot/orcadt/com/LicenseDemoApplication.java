package iot.orcadt.com;

import com.orcadt.license.bean.LicenseAuthHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@SpringBootApplication(scanBasePackages = {"com.orcadt.license", "iot.orcadt.com"})
class LicenseDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseDemoApplication.class, args);
    }

    @GetMapping("test")
    public Object getLicenseResult() {
        return LicenseAuthHolder.authModels;
    }
}
