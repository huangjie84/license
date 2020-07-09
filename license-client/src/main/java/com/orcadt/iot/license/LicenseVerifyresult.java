package com.orcadt.iot.license;

import de.schlichtherle.license.LicenseContent;

/**
 * 定时任务的检查结果
 *
 * @author jie.huang
 * @date 2020/6/29
 **/
public class LicenseVerifyresult {

    /**
     * 是否授权
     */
    public static volatile boolean isVerified = false;

    /**
     * License content
     */
    public static volatile LicenseContent licenseContent = null;
    /**
     * ServerInfo
     */
    public static volatile LicenseCheckModel serverInfo = null;


    /**
     * 授权失败信息
     */
    public static volatile String verifyMsg = "";
}
