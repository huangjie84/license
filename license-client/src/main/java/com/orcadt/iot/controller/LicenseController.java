package com.orcadt.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.orcadt.iot.bean.MsgResonse;
import com.orcadt.iot.license.CustomLicenseManager;
import com.orcadt.iot.license.LicenseCheckModel;
import com.orcadt.iot.license.LicenseInstaller;
import com.orcadt.iot.license.LicenseManagerHolder;
import com.orcadt.iot.license.LicenseVerifyresult;
import com.orcadt.iot.secret.SymmetricEncoder;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
@RestController
@RequestMapping("/license")
public class LicenseController {
    private static Logger logger = LogManager.getLogger(LicenseController.class);

    @Autowired
    private LicenseInstaller installer;

    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 证书路径
     */
    @Value("${license.licensePath}")
    private String licensePath;

    /**
     * 获取服务器硬件信息
     *
     * @return com.ccx.models.license.LicenseCheckModel
     * @since 1.0.0
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_VALUE})
    public LicenseCheckModel getServerInfos() {

        return CustomLicenseManager.getServerInfos();
    }

    @GetMapping("/check")
    public Mono<String> check() {
        String result = Optional.ofNullable(LicenseVerifyresult.licenseContent)
                .filter(e -> LicenseVerifyresult.isVerified)
                .map(LicenseContent::getExtra)
                .filter(Objects::nonNull)
                .map(JSONObject::toJSONString)
                .map(JSONObject::parseObject)
                .map(e -> e.getJSONArray("authModels"))
                .map(JSON::toString)
                .map(e->{
                    HashMap<String, Object> map = new HashMap<>(4);
                    map.put("Obfuscat",new Random().nextGaussian());
                    map.put("timestamp",System.currentTimeMillis());
                    map.put("expireDate",LicenseVerifyresult.licenseContent.getNotAfter());
                    map.put("authModels",e);
                    return map;
                })
                .map(e -> new MsgResonse(200, "success", e))
                .map(JSONObject::toJSONString)
                .map(SymmetricEncoder::AESEncode)
                .orElse(SymmetricEncoder.AESEncode(JSONObject.toJSONString(new MsgResonse(500, "没有获取到有效的授权信息,请重试"))));
        return Mono.just(result);
    }

    @PostMapping("/upload")
    public MsgResonse upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        logger.info("we are dealing upload file:{}", filename);
        if (StringUtils.isBlank(filename) || !"license.lic".equals(filename)) {
            return new MsgResonse(500, "授权文件名称不对.必须为[ license.lic ]");
        }
        String tempPath = licensePath.replace(".lic", "_temp.lic");
        try {

            File tempFile = new File(new URL(tempPath).getFile());
            file.transferTo(tempFile);
            if (installer.install(tempPath)) {
                FileCopyUtils.copy(tempFile, new File(new URL(licensePath).getFile()));
                tempFile.delete();
                LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
                LicenseContent licenseContent = licenseManager.verify();
                logger.info(MessageFormat.format("新证书上传成功!，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
                LicenseVerifyresult.isVerified = true;
                LicenseVerifyresult.licenseContent = licenseContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new MsgResonse(501, "授权文件转存失败!请检查权限");
        } catch (Exception e) {
            e.printStackTrace();
            return new MsgResonse(502, "新证书校验失败,请联系奥卡云检查证书是否有效");
        }
        return new MsgResonse(200, "success");
    }


}
