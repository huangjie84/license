package com.orcadt.iot.listener;

import com.orcadt.iot.license.CustomLicenseManager;
import com.orcadt.iot.license.LicenseCheckModel;
import com.orcadt.iot.license.LicenseInstaller;
import com.orcadt.iot.license.LicenseManagerHolder;
import com.orcadt.iot.license.LicenseVerifyresult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LogManager.getLogger(LicenseCheckListener.class);

    @Autowired
    private LicenseInstaller installer;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LicenseCheckModel serverInfos = CustomLicenseManager.getServerInfos();
        LicenseVerifyresult.serverInfo = serverInfos;
        logger.info("==================server info loaded over!");
        ApplicationContext context = event.getApplicationContext().getParent();

        if (context == null) {
            installer.install(null);
        }
        try {
            LicenseVerifyresult.licenseContent = LicenseManagerHolder.getInstance(null).verify();
        } catch (Exception e) {
            logger.info("++++++++ 加载完毕,读取失败! ++++++++", e);
        }
    }
}
