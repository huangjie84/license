package com.orcadt.iot.license;

import java.util.List;
import java.util.Scanner;

/**
 * @author jie.huang
 * @date 2020/6/29
 **/
public class WindowsServerInfo extends AbstractServerInfo {
    @Override
    protected List<String> getMacInfos() {
        return getLocalMacInfo();
    }

    public static void main(String[] args) throws Exception {
        WindowsServerInfo info = new WindowsServerInfo();
        String cpuSerials = info.getCpuSerials();
        String mainBoardSerial = info.getMainBoardSerial();
        System.out.println(cpuSerials);
        System.out.println(mainBoardSerial);
    }

    @Override
    protected String getMainBoardSerial() throws Exception {
        //序列号
        String serialNumber = "";

        //使用WMIC获取主板序列号
        Process process = Runtime.getRuntime().exec("wmic baseboard get serialnumber");
        process.getOutputStream().close();
        try (Scanner scanner = new Scanner(process.getInputStream())){
            if(scanner != null && scanner.hasNext()){
                scanner.next();
            }

            if(scanner.hasNext()){
                serialNumber = scanner.next().trim();
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

        //使用WMIC获取CPU序列号
        Process process = Runtime.getRuntime().exec("wmic cpu get processorid");
        process.getOutputStream().close();
        try (Scanner scanner = new Scanner(process.getInputStream())){
            if(scanner != null && scanner.hasNext()){
                scanner.next();
            }

            if(scanner.hasNext()){
                serialNumber = scanner.next().trim();
            }
        }

        return serialNumber;
    }
}
