package com.example.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public final class IndexController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/api/v1/order";
    }

}
