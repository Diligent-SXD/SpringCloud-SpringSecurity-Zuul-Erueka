package com.sxd.springcloud.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello User!";
    }

    @GetMapping("/guest")
    public String guest() {
        return "Hello Guest!";
    }
}
