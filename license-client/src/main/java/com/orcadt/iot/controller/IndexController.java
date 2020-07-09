package com.orcadt.iot.controller;

import com.orcadt.iot.license.LicenseVerifyresult;
import de.schlichtherle.license.LicenseContent;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jie.huang
 * @date 2020/6/30
 **/
@Controller
public class IndexController {
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("new");
    }


    @GetMapping("/main")
    public ModelAndView main() {
        ModelAndView view = new ModelAndView("main");
        try {
            LicenseContent licenseContent = LicenseVerifyresult.licenseContent;
            if (licenseContent != null) {
                LicenseContent target = new LicenseContent();
                BeanUtils.copyProperties(licenseContent, target);
                target.setHolder(null);
                target.setIssuer(null);
                Object extra = licenseContent.getExtra();
                target.setExtra(null);
                view.addObject("license", target)
                        .addObject("extra", extra);
            } else {
                view.addObject("errorMsg", "证书信息为空!请检查");
            }

            view.addObject("serverInfos", LicenseVerifyresult.serverInfo);

        } catch (Exception e) {
            e.printStackTrace();
            view.addObject("errorMsg", "加载证书信息异常！");
        }
        return view;

    }
}
