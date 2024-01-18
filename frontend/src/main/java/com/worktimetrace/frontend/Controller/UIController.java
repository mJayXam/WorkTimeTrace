package com.worktimetrace.frontend.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UIController {
    
    @GetMapping("/")
    public String getWebsite() {
        return "index";
    }
    
}
