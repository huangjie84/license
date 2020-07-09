package com.orcadt.iot.config;

import com.alibaba.fastjson.JSONObject;
import com.orcadt.iot.bean.LicenseAuthModel;
import com.orcadt.iot.bean.LicenseAuthHolder;
import com.orcadt.iot.bean.MsgResonse;
import com.orcadt.iot.secret.SuperEncoder;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * 验证逻辑转移至 线程linstener内部
 *
 * @author jie.huang
 * @date 2020/6/30
 **/
@Component
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {
//    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);
//
//    @Value("${license.check.url}")
//    private String licenceCheckUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String msg = "您的证书无效，请核查服务器是否取得授权或重新申请证书！";
//        //校验证书是否有效
//        boolean verifyResult = false;
//        if (!StringUtils.isEmpty(licenceCheckUrl)) {
//            OkHttpClient httpClient = new OkHttpClient();
//            Request okrequest = new Request.Builder().url(licenceCheckUrl).get().build();
//            Call call = httpClient.newCall(okrequest);
//            try {
//                Response execute = call.execute();
//                ResponseBody body = execute.body();
//                String result = SuperEncoder.AESDncode(body.string());
//                logger.info("同步结果----  {} ", result + "");
//                MsgResonse msgResonse = JSONObject.parseObject(result, MsgResonse.class);
//                if (msgResonse != null) {
//                    if (msgResonse.getCode() == 200) {
//                        verifyResult = true;
//                        LicenseAuthHolder.authModels = Optional.ofNullable(msgResonse.getData())
//                                .map(Object::toString)
//                                .map(e -> JSONObject.parseArray(e, LicenseAuthModel.class))
//                                .orElse(null);
//                    } else {
//                        logger.error("证书服务器返回错误:{}", msgResonse.getMsg());
//                    }
//                }
//            } catch (IOException e) {
//                logger.error("证书请求结果失败!", e);
//            }
//        }
//        if (verifyResult) {
//            return super.preHandle(request, response, handler);
//        } else {
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json");
//            response.getWriter()
//                    .write(new MsgResonse(504, msg).toJsonString());
//            return false;
//        }
        return super.preHandle(request, response, handler);
    }
}
