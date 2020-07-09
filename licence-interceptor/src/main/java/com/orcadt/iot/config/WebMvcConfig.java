package com.orcadt.iot.config;

import com.orcadt.iot.util.SpringContextUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LicenseCheckInterceptor interceptor = SpringContextUtils.getBeanByClass(LicenseCheckInterceptor.class);
        registry.addInterceptor(interceptor).addPathPatterns("/check");
    }
}
