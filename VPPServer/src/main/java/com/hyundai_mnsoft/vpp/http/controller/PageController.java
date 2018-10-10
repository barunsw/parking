package com.hyundai_mnsoft.vpp.http.controller;

import com.hyundai_mnsoft.vpp.tcp.RmiControl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping(value = "/")
    public String test(){
        try {
            System.out.println(RmiControl.test("11"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
}
