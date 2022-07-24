package com.kyc.aggregate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class HomeController
{

    @RequestMapping("/")
    public String home()
    {
    	System.out.println("----home mtd");
        return "redirect:swagger-ui.html";
    }

    @RequestMapping("/kyc1")
    public String test()
    {
    	System.out.println("----test mtd");
        return "redirect:swagger-ui.html";
    }
}


