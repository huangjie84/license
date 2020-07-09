package com.orcadt.iot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

/**
 * @author jie.huang
 * @date 2020/7/7
 **/
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void downloadFile(String filePath, String downloadName,HttpServletResponse response) throws Exception {
        downloadName = URLDecoder.decode(downloadName,"utf-8");
        response.reset();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;filename="+new String(downloadName.getBytes("GBK"),"iso8859-1"));

        try (OutputStream out = response.getOutputStream();
             InputStream in = new BufferedInputStream(new FileInputStream(filePath))
        ) {
            byte[] buff = new byte[1024];
            int i = 0;
            while ((i = in.read(buff))!= -1){
                out.write(buff,0,i);
                out.flush();
            }
        }catch (Exception e){
            logger.error("export file:{},error",e);
        }
    }
}
