package com.orcadt.iot.quartz;

import com.orcadt.iot.license.LicenseManagerHolder;
import com.orcadt.iot.license.LicenseVerifyresult;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
@Component
public class CheckLicenseQuartz {
    private static Logger logger = LogManager.getLogger(CheckLicenseQuartz.class);
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkOnTime() {
        logger.info("we are checking verify file!");
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            LicenseVerifyresult.isVerified = true;
            LicenseVerifyresult.licenseContent = licenseContent;
            LicenseVerifyresult.verifyMsg = "";
        } catch (Exception e) {
            logger.error("证书校验失败！", e);
            LicenseVerifyresult.isVerified = false;
            LicenseVerifyresult.verifyMsg = "证书校验失败！";
        }
    }
}
