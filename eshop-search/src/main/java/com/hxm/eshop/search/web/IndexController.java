package com.hxm.eshop.search.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping(value = {"/", "index.html"})
    private String indexPage(Model model) {
        return "index";
    }
}
