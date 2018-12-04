package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


public class ControlServerUtil {
    private static Logger LOGGER = Logger.getLogger(ControlServerUtil.class);

    private Properties props;

    private String dev_ip;
    private String dev_ui_port;
    private String dev_ny_port;
    private String dev_ss_port;

    ControlServerUtil() {
        Resource resource = new ClassPathResource("/config.properties");
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
            dev_ip = props.getProperty("controlServer.dev.ip");
            dev_ui_port = props.getProperty("controlServer.dev.ui.port");
            dev_ny_port = props.getProperty("controlServer.dev.ny.port");
            dev_ss_port = props.getProperty("controlServer.dev.ss.port");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public String getResFromControlServer(String path) {
//        return getResString(path, "{}");
//    }

    String getResFromControlServer(String parkingAreaID, String path, String param) {
        if (param == null) {
            param = "{}";
        }
        return getResString(parkingAreaID, path, param);
    }

    private String getResString(String parkingAreaID, String path, String s) {
        HttpClient client = null;

        try {
            client = new DefaultHttpClient();

            String port;
            switch (parkingAreaID) {
                case "01":
                    port = dev_ny_port;
                    break;
                case "02":
                    port = dev_ui_port;
                    break;
                case "03":
                    port = dev_ss_port;
                    break;
                default:
                    throw new Exception("parkingAreaID가 정의되지 않았습니다.");
//                    port = "";
                    //에러로 빠질것.
            }

            String url = "http://" + dev_ip + ":" + port + path;

            LOGGER.debug(url);

            HttpPost post = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(1*1000)
                    .setConnectTimeout(1*1000)
                    .setConnectionRequestTimeout(1*1000)
                    .build();

            post.setConfig(requestConfig);

            post.setEntity(new StringEntity(s, ContentType.APPLICATION_JSON));

            HttpResponse response = client.execute(post);
            response.setHeader("Content-Type", "application/json; charset=UTF-8");

            LOGGER.debug("Sending 'POST' request to URL : " + url);
            LOGGER.debug("Post parameters : " + post.getEntity());
            LOGGER.debug("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            LOGGER.debug("Result\n" + result.toString());

            return result.toString();
        }catch (Exception e){
            e.printStackTrace();

            return "";
        }
        finally {
            if (client != null) {
                ((DefaultHttpClient) client).close();
            }
        }
    }

}
