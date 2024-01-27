package com.worktimetrace.frontend.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktimetrace.frontend.Models.CalendarMonth;
import com.worktimetrace.frontend.Models.LoginData;
import com.worktimetrace.frontend.Models.User;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UIController {

    @GetMapping("/")
    public String showLandingPage(Model model) {
        model.addAttribute("landingPage", true);
        return "index";
    }

    @GetMapping("/registration")
    public String showRegistrationView(Model model) {
        model.addAttribute("registration", true);
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/registration")
    public String getRegistrationData(@ModelAttribute("user") User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Objekt in JSON-String umwandeln
            String requestJson= objectMapper.writeValueAsString(user);
            String response = sendPostRequestToOtherService("https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/register", requestJson);
            
            // JSON-String ausgeben
            System.out.println(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginView(Model model) {
        model.addAttribute("login", true);
        model.addAttribute("logindata", new LoginData());
        return "index";
    }

    @PostMapping("/login")
    public String getLoginData(@ModelAttribute("logindata") LoginData loginData) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Objekt in JSON-String umwandeln
            String requestJson= objectMapper.writeValueAsString(loginData);
            String response = sendPostRequestToOtherService("https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/login", requestJson);
            
            // JSON-String ausgeben
            System.out.println(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/kalender")
    public String showKalenderView(@RequestParam(name = "month", required = false) int parameter, Model model) {
        String[] dayStrings = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
        ArrayList<String> days = new ArrayList<>(Arrays.asList(dayStrings));

        if (parameter == 0) {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now());
            model.addAttribute("kalender", true);
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", calendarMonth.getMonthYear());
            model.addAttribute("nextMonthUrl", "kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "kalender?month=" + (parameter - 1));
        } else {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now().plusMonths(parameter));
            model.addAttribute("kalender", true);
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", calendarMonth.getMonthYear());
            model.addAttribute("nextMonthUrl", "kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "kalender?month=" + (parameter - 1));
        }

        return "index";
    }

    @GetMapping("/monatsuebersicht")
    public String showMonatsuebersichtView(Model model) {
        model.addAttribute("monatsuebersicht", true);
        model.addAttribute("title", "Monatsübersicht");
        return "index";
    }


    public String sendPostRequestToOtherService(String url, String body) {
        RestTemplate restTemplate = new RestTemplate();

        // Annahme: Du möchtest Daten im JSON-Format senden
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Führe die POST-Anfrage durch
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        String responseBody = response.getBody();

        return responseBody;
    }
}
