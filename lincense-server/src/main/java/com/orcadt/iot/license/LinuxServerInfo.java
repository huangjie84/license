package com.orcadt.iot.license;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author jie.huang
 * @date 2020/6/28
 **/
public class LinuxServerInfo extends AbstractServerInfo {
    @Override
    protected List<String> getMacInfos() {
        List<String> localMacInfo = getLocalMacInfo();
        return localMacInfo;
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        //序列号
        String serialNumber = "";

        //使用dmidecode命令获取主板序列号
        String[] shell = {"/bin/bash","-c","dmidecode | grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine().trim();
            if(StringUtils.isNotBlank(line)){
                serialNumber = line;
            }
        }
        return serialNumber;
    }

    @Override
    protected List<String> getIpList() throws Exception {
        return getLocalIpInfo();
    }

    @Override
    protected String getCpuSerials() throws Exception {
        //序列号
        String serialNumber = "";
        //使用dmidecode命令获取CPU序列号
        String[] shell = {"/bin/bash","-c","dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine().trim();
            if(StringUtils.isNotBlank(line)){
                serialNumber = line;
            }
        }
        return serialNumber;
    }


}
