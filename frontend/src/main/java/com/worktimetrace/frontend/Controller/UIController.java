package com.worktimetrace.frontend.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.worktimetrace.frontend.Models.Datum;




@Controller
public class UIController {
    
    @GetMapping("/")
    public String showLandingPage(Model model) {
        model.addAttribute("landingPage", true);
        return "index";
    }

    @GetMapping("/Kalender")
    public String showKalenderView(Model model) {
        ArrayList<Datum> daysInMonth = Datum.daysInMonthArray(LocalDate.now());
        String [] dayStrings = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
        ArrayList<String> days = new ArrayList<>(Arrays.asList(dayStrings));
        model.addAttribute("kalender", true);
        model.addAttribute("kalenderTage", days);
        model.addAttribute("ersteWoche", Datum.getFirstWeekOfGivenMonth(daysInMonth));
        model.addAttribute("zweiteWoche", Datum.getSeccondWeekOfGivenMonth(daysInMonth));
        model.addAttribute("dritteWoche", Datum.getThirdWeekOfGivenMonth(daysInMonth));
        model.addAttribute("vierteWoche", Datum.getFourthWeekOfGivenMonth(daysInMonth));
        model.addAttribute("fuenfteWoche", Datum.getFifthWeekOfGivenMonth(daysInMonth));
        return "index";
    }
    

    @GetMapping("/Monatsuebersicht")
    public String showMonatsuebersichtView(Model model) {
        model.addAttribute("monatsuebersicht", true);
        return "index";
    }
    
}
