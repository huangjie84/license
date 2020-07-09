package com.orcadt.iot.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.orcadt.iot.bean.MsgResonse;
import com.orcadt.iot.license.LicenseVerify;
import com.orcadt.iot.license.LicenseVerifyresult;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.NoLicenseInstalledException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LicenseCheckInterceptor
 *
 * @since 1.0.0
 */
public class LicenseCheckInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LogManager.getLogger(LicenseCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String msg = "您的证书无效，请核查服务器是否取得授权或重新申请证书！";
        //校验证书是否有效
        boolean verifyResult = false;
        try {
            // 如果校验结果失败,则再次检查是否为
            if (LicenseVerifyresult.isVerified || new LicenseVerify().verify()) {
                verifyResult = true;
            }
        } catch (NoLicenseInstalledException e1) {
            msg = "证书安装失败!请核查服务器是否取得授权或重新申请证书！";
            logger.error("证书安装失败", e1);
        } catch (LicenseContentException e2) {
            msg = "证书已失效:原因[" + e2.getMessage() + "]";
            logger.error("证书已失效", e2);
        } catch (Exception e3) {
            msg = "证书无法识别.请检查";
            logger.error("证书识别失败", e3);
        }

        if (verifyResult) {
            return true;
        } else {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            response.getWriter()
                    .write(JSONObject.toJSONString(
                            new MsgResonse(504, msg)));
            return false;
        }
    }

}
