package com.hyundai_mnsoft.vpp.biz.http.service.controlserver;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class ControlServerUtil {

    private Properties props;

    private String ip;
    private String port;

    public ControlServerUtil() {
        Resource resource = new ClassPathResource("/config.properties");
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
            ip = props.getProperty("controlServer.ip");
            port = props.getProperty("controlServer.port");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResFromControlServer(String path) {
        return getResString(path, "{}");
    }

    public String getResFromControlServer(String path, String param) {
        return getResString(path, param);
    }

    private String getResString(String path, String s) {
        try {
            HttpClient client = new DefaultHttpClient();

            String url = "http://" + ip + ":" + port + path;

            System.out.println(url);
            HttpPost post = new HttpPost(url);

            post.setEntity(new StringEntity(s, ContentType.APPLICATION_FORM_URLENCODED));
            post.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " +
                    response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());

            return result.toString();
        }catch (Exception e){
            e.printStackTrace();

            return "";
        }
    }

}
