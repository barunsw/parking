package com.hyundai_mnsoft.vpp.biz.http.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ControlServerUtil {

//    @Value("#{config['controlServer.ip']}")
    private String ip = "125.143.164.73";

//    @Value("#{config['controlServer.port']}")
    private String port = "9003";

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
