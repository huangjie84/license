package com.orcadt.license.listener;

import com.alibaba.fastjson.JSONObject;
import com.orcadt.license.bean.LicenseAuthHolder;
import com.orcadt.license.bean.LicenseAuthModel;
import com.orcadt.license.bean.MsgResonse;
import com.orcadt.license.secret.SuperEncoder;
import com.orcadt.license.util.SpringContextUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jie.huang
 * @date 2020/7/1
 **/
@Component
public class LicenseCheckListener implements ApplicationListener<ContextRefreshedEvent> {

    //程序启动时给予校验license的机会
    private static AtomicInteger initTimes = new AtomicInteger(3);
    private static Logger logger = LogManager.getLogger(LicenseCheckListener.class);
    @Value("${license.check.url:http://null}")
    private String licenceCheckUrl;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info(">>>>>>>>>>>>>license checker starting!<<<<<<<<<<<<<<<<<<<<<");
        new Thread(() -> {
            for (; ; ) {
                boolean result = checkLicense();
                if (!result && initTimes.addAndGet(-1) <= 0) {
                    logger.error("\r>>>>>>>>>>>>>checking license form:[{}] <<<<<<<<<<<<<<<<<<<<<" +
                            "\r>>>>>>>>>>>>>checking result:{} <<<<<<<<<<<<<<<<<<<<<" +
                            "\r ============  some thing error.....============== \n" +
                            "\r ============= we are shutting down the server!============================",licenceCheckUrl,result);
                    ApplicationContext context = SpringContextUtils.getApplicationContext();
                    int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);
                    System.exit(exitCode);
                }
                try {
                    Thread.sleep(1000 * 30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    boolean checkLicense() {
        //校验证书是否有效
        AtomicBoolean verifyResult = new AtomicBoolean(false);
        if (!StringUtils.isEmpty(licenceCheckUrl)) {
            OkHttpClient httpClient = new OkHttpClient();
            Request okrequest = new Request.Builder().url(licenceCheckUrl).get().build();
            Call call = httpClient.newCall(okrequest);
            try {
                Response execute = call.execute();
                ResponseBody body = execute.body();
                String resultMsg = body.string();
                String result = SuperEncoder.AESDncode(resultMsg);
                MsgResonse msgResonse = JSONObject.parseObject(result, MsgResonse.class);
                if (msgResonse != null) {
                    if (msgResonse.getCode() == 200) {

                        Optional.ofNullable(msgResonse.getData())
                                .map(Object::toString)
                                .map(JSONObject::parseObject)
                                .ifPresent(e -> {
                                    LicenseAuthHolder.authModels = JSONObject.parseArray(e.getString("authModels"), LicenseAuthModel.class);
                                    Date expireDate = new Date(e.getLong("expireDate"));
                                    Date now = new Date();
                                    if (expireDate.after(now)) {
                                        verifyResult.set(true);
                                    } else {
                                        logger.error("证书已过期,当前时间:{},授权截止时间:{}", now, expireDate);
                                    }
                                });
                    } else {
                        logger.error("证书服务器返回错误:{}", msgResonse.getMsg());
                    }
                }
            } catch (Exception e) {
                logger.error("证书请求结果失败!", e);
            }
        } else {
            logger.error("\n=========================\r===========您还没有配置证书路径=========\r=============");
        }
        return verifyResult.get();
    }
}
