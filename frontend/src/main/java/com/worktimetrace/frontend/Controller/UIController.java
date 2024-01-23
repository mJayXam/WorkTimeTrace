package com.worktimetrace.frontend.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.worktimetrace.frontend.Models.CalendarMonth;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class UIController {
    
    @GetMapping("/")
    public String showLandingPage(Model model) {
        model.addAttribute("landingPage", true);
        return "index";
    }

    @GetMapping("/Kalender")
    public String showKalenderView(@RequestParam(name = "month", required = false) int parameter, Model model) {
        String [] dayStrings = {"Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"};
        ArrayList<String> days = new ArrayList<>(Arrays.asList(dayStrings));
        

        if(parameter == 0)
        {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now());
            model.addAttribute("kalender", true);
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", new CalendarMonth(LocalDate.now()).getMonthYear());
            model.addAttribute("nextMonthUrl", "Kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "Kalender?month=" + (parameter - 1));
        }
        else
        {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now().plusMonths(parameter));
            model.addAttribute("kalender", true);
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", new CalendarMonth(LocalDate.now().plusMonths(parameter)).getMonthYear());
            model.addAttribute("nextMonthUrl", "Kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "Kalender?month=" + (parameter - 1));
        }
        
        return "index";
    }

    @GetMapping("/Monatsuebersicht")
    public String showMonatsuebersichtView(Model model) {
        model.addAttribute("monatsuebersicht", true);
        model.addAttribute("title", "Monatsübersicht");
        return "index";
    }
    
}
