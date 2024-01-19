package com.worktimetrace.frontend.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class UIController {
    
    @GetMapping("/")
    public String getWebsite(Model model) {
        model.addAttribute("kalender", true);
        return "index";
    }

    @GetMapping("/Monatsuebersicht")
    public String getMonatsuebersicht(Model model) {
        model.addAttribute("monatsuebersicht", true);
        return "index";
    }
    
    
}
