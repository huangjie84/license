package com.orcadt.iot.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * @author jie.huang
 * @date 2020/6/28
 **/

public abstract class AbstractServerInfo {
    private Logger log = LoggerFactory.getLogger(AbstractServerInfo.class);

    public LicenseCheckModel getServerInfos() {
        try {
            LicenseCheckModel model = new LicenseCheckModel();
            model.setCpuSerial(this.getCpuSerials());
            model.setIpList(this.getIpList());
            model.setMacList(this.getMacInfos());
            model.setMainBoardSerial(this.getMainBoardSerial());
            return model;
        } catch (Exception e) {
            log.error("获取服务器信息失败!");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    protected abstract List<String> getMacInfos();

    /**
     * 获取主板地址
     *
     * @return
     * @throws Exception
     */
    protected abstract String getMainBoardSerial() throws Exception;

    /**
     * 获取ip'
     *
     * @return
     * @throws Exception
     */
    protected abstract List<String> getIpList() throws Exception;

    /**
     * 获取cpu信息
     *
     * @return
     * @throws Exception
     */
    protected abstract String getCpuSerials() throws Exception;

    /**
     * 获取本地mac信息
     *
     * @return
     */
    protected List<String> getLocalMacInfo() {
        List<String> macList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> em = NetworkInterface.getNetworkInterfaces();
            while (em.hasMoreElements()) {
                NetworkInterface anInterface = em.nextElement();
                byte[] hardwareAddress = anInterface.getHardwareAddress();
                if (hardwareAddress == null) {
                    continue;
                }
                StringJoiner joiner = new StringJoiner("-");
                for (byte e : hardwareAddress) {
                    String format = String.format("%02X", e);
                    joiner.add(format);
                }
                String macStr = joiner.toString().toUpperCase();
                macList.add(macStr);
            }
        } catch (SocketException e) {
            log.error("==========> socket exception! can't get socket!");
            e.printStackTrace();
        }
        log.info("------------> we get mac list:{}", macList);
        return macList;
    }

    /**
     * 获取本地IP信息
     * @return
     */
    protected List<String> getLocalIpInfo() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> em = NetworkInterface.getNetworkInterfaces();
            while (em.hasMoreElements()) {
                NetworkInterface anInterface = em.nextElement();
                Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses();
                for (InetAddress inetAddress = inetAddresses.nextElement(); inetAddresses.hasMoreElements(); ) {
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()
                            && !inetAddress.isMulticastAddress()) {
                        ipList.add(inetAddress.getHostAddress());
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList.stream().distinct().map(String::toLowerCase).collect(Collectors.toList());
    }

}
