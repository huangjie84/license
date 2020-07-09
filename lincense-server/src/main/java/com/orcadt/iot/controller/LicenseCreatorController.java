package com.orcadt.iot.controller;

import com.alibaba.fastjson.JSONObject;
import com.orcadt.iot.bean.MsgResonse;
import com.orcadt.iot.common.utils.R;
import com.orcadt.iot.entity.LicenseGenerateRecordEntity;
import com.orcadt.iot.license.AbstractServerInfo;
import com.orcadt.iot.license.LicenseAuthModel;
import com.orcadt.iot.license.LicenseCheckModel;
import com.orcadt.iot.license.LicenseCreator;
import com.orcadt.iot.license.LicenseCreatorParam;
import com.orcadt.iot.license.LinuxServerInfo;
import com.orcadt.iot.license.WindowsServerInfo;
import com.orcadt.iot.service.LicenseGenerateRecordService;
import com.orcadt.iot.util.FileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
@RestController
@RequestMapping("/license")
public class LicenseCreatorController {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 证书路径
     */
    @Value("${license.licensePath}")
    private String licensePath;


    /**
     * 证书路径
     */
    @Value("${license.privateKeysStorePath}")
    private String privateKeysStore;

    @Autowired
    private LicenseGenerateRecordService generateRecordService;

    /**
     * 获取服务器硬件信息
     *
     * @param osName 操作系统类型，如果为空则自动判断
     * @return com.ccx.models.license.LicenseCheckModel
     * @author zifangsky
     * @date 2018/4/26 13:13
     * @since 1.0.0
     */
    @RequestMapping(value = "/getServerInfos", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public LicenseCheckModel getServerInfos(@RequestParam(value = "osName", required = false) String osName) {
        //操作系统类型
        if (StringUtils.isBlank(osName)) {
            osName = System.getProperty("os.name");
        }
        osName = osName.toLowerCase();

        AbstractServerInfo abstractServerInfos = null;

        //根据不同操作系统类型选择不同的数据获取方法
        if (osName.startsWith("windows")) {
            abstractServerInfos = new WindowsServerInfo();
        } else if (osName.startsWith("linux")) {
            abstractServerInfos = new LinuxServerInfo();
        } else {//其他服务器类型
            abstractServerInfos = new LinuxServerInfo();
        }

        return abstractServerInfos.getServerInfos();
    }
    @RequestMapping(value = "/downloadLicense")
    public R downloadLicense(@RequestParam("recordId")String recordId, HttpServletResponse response) throws Exception {
        LicenseGenerateRecordEntity record = generateRecordService.getById(recordId);
        if (record == null || StringUtils.isBlank(record.getLicensePath())){
            return R.error(505,"需要下载的licens记录不存在或license路径为空!");
        }
        FileUtil.downloadFile(record.getLicensePath(),"license.lic",response);
        return null;
    }

    /**
     * 生成证书
     *
     * @param param 生成证书需要的参数，如：{"subject":"ccx-models","privateAlias":"privateKey","keyPass":"5T7Zz5Y0dJFcqTxvzkH5LDGJJSGMzQ","storePass":"3538cef8e7","licensePath":"C:/Users/zifangsky/Desktop/license.lic","privateKeysStorePath":"C:/Users/zifangsky/Desktop/privateKeys.keystore","issuedTime":"2018-04-26 14:48:12","expiryTime":"2018-12-31 00:00:00","consumerType":"User","consumerAmount":1,"description":"这是证书描述信息","licenseCheckModel":{"ipAddress":["192.168.245.1","10.0.5.22"],"macAddress":["00-50-56-C0-00-01","50-7B-9D-F9-18-41"],"cpuSerial":"BFEBFBFF000406E3","mainBoardSerial":"L1HF65E00X9"}}
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zifangsky
     * @date 2018/4/26 13:13
     * @since 1.0.0
     */
    @RequestMapping(value = "/generateLicense", produces = {MediaType.APPLICATION_JSON_VALUE})
    public MsgResonse generateLicense(@RequestBody @Validated LicenseCreatorParam param, HttpServletResponse response) {
        String resultMsg = checkeParams(param);
        try {
            param.setIssuedTime(sdf.parse(param.getIssuedTimeStr()));
            param.setExpiryTime(sdf.parse(param.getExpiryTimeStr()));
        }catch (Exception e){
            return new MsgResonse(9002, "日期格式不合法");
        }

        if (resultMsg!=null) {
            return new MsgResonse(9005, resultMsg);
        }
        Date expiryTime = param.getExpiryTime();
        if (expiryTime == null || expiryTime.before(new Date())){
            return new MsgResonse(9001, "过期时间不能小于当前时间");
        }
        if (!expiryTime.after(param.getIssuedTime())){
            return new MsgResonse(9003, "过期时间必須大于授权时间");
        }

        param.setSubject("orcadt_license");
        param.setPrivateAlias("privateKey");
        param.setKeyPass("private_orcadt_key_123456");
        param.setStorePass("public_orcadt_key_654321");
        param.setLicensePath(licensePath+"license_"+System.currentTimeMillis()+".lic");
        param.setPrivateKeysStorePath(privateKeysStore);
        param.setConsumerAmount(1);
        param.setConsumerType("User");

        LicenseCreator licenseCreator = new LicenseCreator(param);
        boolean result = licenseCreator.generateLicense();
        if (result) {
            LicenseGenerateRecordEntity entity = new LicenseGenerateRecordEntity();
            entity.setConsumerAmount(param.getConsumerAmount());
            entity.setConsumerType(param.getConsumerType());
            entity.setIssuedTime(sdf.format(param.getIssuedTime()));
            entity.setExpiryTime(sdf.format(param.getExpiryTime()));
            entity.setSubject(param.getSubject());
            entity.setDescription(param.getDescription());
            entity.setKeyPass(param.getKeyPass());
            entity.setPrivateKeysStorePath(param.getPrivateKeysStorePath());
            entity.setLicensePath(param.getLicensePath());
            entity.setLicenseCheckModel(JSONObject.toJSONString(param.getLicenseCheckModel()));
            entity.setStorePass(param.getStorePass());
            generateRecordService.save(entity);
            return new MsgResonse(200, "OK",entity.getRecordId());
        } else {
            return new MsgResonse(1000, "证书文件生成失败！");
        }

    }

    private String checkeParams(LicenseCreatorParam param) {

        if (StringUtils.isBlank(param.getDescription())){
            return "证书备注不能为空!";
        }else if (StringUtils.isBlank(param.getLicenseCheckModel().getCpuSerial())){
            return "CPU序列号不能为空!";
        }else if (StringUtils.isBlank(param.getLicenseCheckModel().getMainBoardSerial())){
            return "主板序列号不能为空!";
        }else if (CollectionUtils.isEmpty(param.getLicenseCheckModel().getMacList())){
            return "MAC列表不能为空!";
        }
        List<String> realMac = param.getLicenseCheckModel()
                .getMacList().stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(realMac)){
            return "MAC列表不能为空值!";
        }
        List<LicenseAuthModel> authModels = param.getLicenseCheckModel().getAuthModels();
        if (authModels!=null){
            authModels=authModels.parallelStream()
                    .filter(e->
                        StringUtils.isNotBlank(e.getKey())
                            &&StringUtils.isNotBlank(e.getName())
                            && Objects.nonNull(e.getValue())
                            && StringUtils.isNotBlank(e.getValue().toString())
                    )
                    .collect(Collectors.toList());
            param.getLicenseCheckModel().setAuthModels(authModels);
        }
        return null;
    }


}
