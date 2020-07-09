package com.orcadt.iot.license;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 *
 * @since 1.0.0
 */
public class LicenseVerify {
    private static Logger logger = LogManager.getLogger(LicenseVerify.class);

    /**
     * 安装License证书
     *
     * @since 1.0.0
     */
    public synchronized LicenseContent install(LicenseVerifyParam param) {
        LicenseContent result = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //1. 安装证书
        try {
//            URL licnesePath = getClass().getClassLoader().getResource(param.getLicensePath());
            // 因为要替换lic文件,所以只能用绝对路径获取
            URL licnesePath = new URL(param.getLicensePath());
//            URL storePath = getClass().getClassLoader().getResource(param.getPublicKeysStorePath());
            URL storePath = new URL(param.getPublicKeysStorePath());
            param.setLicensePath(licnesePath.getFile());
            param.setPublicKeysStorePath(storePath.getFile());
            logger.error("========>  installing public keys:[{}]", param.getPublicKeysStorePath());
            LicenseManager licenseManager = LicenseManagerHolder.getInstance(initLicenseParam(param));
            licenseManager.uninstall();

            result = licenseManager.install(new File(param.getLicensePath()));
            logger.info(MessageFormat.format("证书安装成功，证书有效期：{0} - {1}", format.format(result.getNotBefore()), format.format(result.getNotAfter())));
        } catch (Exception e) {
            logger.error("证书安装失败！", e);
        }

        return result;
    }

    /**
     * 校验License证书
     *
     * @return boolean
     * @since 1.0.0
     */
    public boolean verify() throws Exception {
        LicenseManager licenseManager = LicenseManagerHolder.getInstance(null);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //2. 校验证书
        try {
            LicenseContent licenseContent = licenseManager.verify();
            logger.info(MessageFormat.format("证书校验通过，证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
            return true;
        } catch (Exception e) {
            logger.error("证书校验失败！", e);
            throw e;
        }
    }

    /**
     * 初始化证书生成参数
     *
     * @param param License校验类需要的参数
     * @return de.schlichtherle.license.LicenseParam
     * @since 1.0.0
     */
    private LicenseParam initLicenseParam(LicenseVerifyParam param) {
        Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

        CipherParam cipherParam = new DefaultCipherParam(param.getStorePass());

        KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class
                , param.getPublicKeysStorePath()
                , param.getPublicAlias()
                , param.getStorePass()
                , null);

        return new DefaultLicenseParam(param.getSubject()
                , preferences
                , publicStoreParam
                , cipherParam);
    }

}
