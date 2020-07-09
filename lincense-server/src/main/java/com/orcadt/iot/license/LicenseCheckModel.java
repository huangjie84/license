package com.orcadt.iot.license;

import java.io.Serializable;
import java.util.List;

/**
 * @author jie.huang
 * @date 2020/6/28
 **/
public class LicenseCheckModel implements Serializable {

    /**
     * 当前主机的mac
     */
    private List<String> macList;
    /**
     * 当前主机的ip
     */
    private List<String> ipList;
    /**
     * 当前主机的cpu
     */
    private String cpuSerial;
    /**
     * 当前主机的主板序列号
     */
    private String mainBoardSerial;

    /**
     * 其他需要授权的信息
     */
    private List<LicenseAuthModel> authModels;

    public List<LicenseAuthModel> getAuthModels() {
        return authModels;
    }

    public void setAuthModels(List<LicenseAuthModel> authModels) {
        this.authModels = authModels;
    }

    public List<String> getMacList() {
        return macList;
    }

    public void setMacList(List<String> macList) {
        this.macList = macList;
    }

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }

    public String getCpuSerial() {
        return cpuSerial;
    }

    public void setCpuSerial(String cpuSerial) {
        this.cpuSerial = cpuSerial;
    }

    public String getMainBoardSerial() {
        return mainBoardSerial;
    }

    public void setMainBoardSerial(String mainBoardSerial) {
        this.mainBoardSerial = mainBoardSerial;
    }

    @Override
    public String toString() {
        return "LicenseCheckModel{" +
                "macList=" + macList +
                ", ipList=" + ipList +
                ", cpuSerial='" + cpuSerial + '\'' +
                ", mainBoardSerial='" + mainBoardSerial + '\'' +
                '}';
    }
}
