package com.orcadt.iot.license;

import com.orcadt.iot.controller.LicenseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jie.huang
 * @date 2020/6/28
 **/
public class LinuxServerInfo extends AbstractServerInfo {


    private static Logger logger = LogManager.getLogger(LicenseController.class);

    @Override
    protected List<String> getMacInfos() {
        return getLocalMacInfo();
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        //序列号
        String serialNumber = "";

        //使用dmidecode命令获取主板序列号
        String[] shell = {"/bin/bash", "-c", "dmidecode -t baseboard| grep 'Serial Number' | awk -F ':' '{print $2}' | head -n 1"};
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine().trim();
            if (StringUtils.isNotBlank(line)) {
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
        String[] shell = {"/bin/bash", "-c", "dmidecode -t processor | grep 'ID' | awk -F ':' '{print $2}' | head -n 1"};
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine().trim();
            if (StringUtils.isNotBlank(line)) {
                serialNumber = line;
            }
        }
        return serialNumber;
    }

    @Override
    protected List<String> getLocalMacRealName() throws Exception {
        String[] shell = {"/bin/bash", "-c", "ls /etc/sysconfig/network-scripts/ifcfg-* | awk -F ':' '{print $1}'"};
        Process process = Runtime.getRuntime().exec(shell);
        process.getOutputStream().close();
        List<String> macList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;

            while (StringUtils.isNotBlank(line = reader.readLine().trim())){
                String[] split = line.split("-");
                String macName = split[split.length-1];
                if (!macName.equalsIgnoreCase("lo")){
                    macList.add(macName);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return macList;
    }


}
