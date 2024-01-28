package com.worktimetrace.frontend.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.worktimetrace.frontend.Models.UserToken;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UIController {

    @GetMapping("/")
    public String showLandingPage(Model model, HttpSession session) {
        Boolean logedIn = (Boolean) session.getAttribute("loginSuccess");

        if (logedIn != null && logedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
        } else {
            session.invalidate();
        }
        model.addAttribute("landingPage", true);
        return "index";
    }

    @GetMapping("/registration")
    public String showRegistrationView(Model model, HttpSession session) {
        String registrationError = (String) session.getAttribute("registrationError");
        User user = new User();
        user.setFirstname((String) session.getAttribute("vorname"));
        user.setLastname((String) session.getAttribute("nachname"));
        user.setUsername((String) session.getAttribute("benutzername"));
        user.setStreet((String) session.getAttribute("strasse"));
        user.setHousenumber((String) session.getAttribute("hausnr"));
        user.setZipcode((String) session.getAttribute("postleitzahl"));
        user.setCity((String) session.getAttribute("stadt"));
        model.addAttribute("registrationError", registrationError);
        model.addAttribute("user", user);
        model.addAttribute("registration", true);
        model.addAttribute("title", "Registrierung");
        return "index";
    }

    @PostMapping("/registration")
    public String getRegistrationData(@ModelAttribute("user") User user, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            String requestJsonRegistration = objectMapper.writeValueAsString(user);
            ResponseEntity<String> responseRegistration = sendPostRequestToOtherService(
                    "https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/register", requestJsonRegistration);

            HttpStatusCode httpStatusCodeRegistration = responseRegistration.getStatusCode();
            if (httpStatusCodeRegistration == HttpStatus.OK) {
                LoginData loginData = new LoginData(user.getUsername(), user.getPassword());
                String requestJsonLogin = objectMapper.writeValueAsString(loginData);
                ResponseEntity<String> responseLogin = sendPostRequestToOtherService(
                        "https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/login", requestJsonLogin);
                HttpStatusCode httpStatusCodeLogin = responseLogin.getStatusCode();
                if (httpStatusCodeLogin == HttpStatus.OK) {
                    UserToken userToken = objectMapper.readValue(responseLogin.getBody(), UserToken.class);
                    session.setAttribute("username", userToken.getUsername());
                    session.setAttribute("token", userToken.getToken());
                    session.setAttribute("loginSuccess", true);

                    return "redirect:/kalender?month=0";
                }
            }
        } catch (HttpClientErrorException.Unauthorized unauthorizedException) {
            session.setAttribute("registrationError", "Ungültiger Benutzername oder Passwort");
            session.setAttribute("vorname", user.getFirstname());
            session.setAttribute("nachname", user.getLastname());
            session.setAttribute("benutzername", user.getUsername());
            session.setAttribute("strasse", user.getStreet());
            session.setAttribute("hausnr", user.getHousenumber());
            session.setAttribute("postleitzahl", user.getZipcode());
            session.setAttribute("stadt", user.getCity());
            return "redirect:/registration";
        } catch (JsonProcessingException e) {
            return "redirect:/registration";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginView(Model model, HttpSession session) {
        LoginData loginData = new LoginData();
        String loginError = (String) session.getAttribute("loginError");

        if(loginError != null) {
            model.addAttribute("loginError", loginError);
        } else {
            model.addAttribute("loginError", null);
        }

        loginData.setUsername((String) session.getAttribute("benutzername"));
        model.addAttribute("login", true);
        model.addAttribute("title", "Login");
        model.addAttribute("logindata", loginData);
        return "index";
    }

    @PostMapping("/login")
    public String getLoginData(@ModelAttribute("logindata") LoginData loginData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(loginData);
            ResponseEntity<String> response = sendPostRequestToOtherService(
                    "https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/login", requestJson);

            HttpStatusCode httpStatusCode = response.getStatusCode();

            if (httpStatusCode == HttpStatus.OK) {
                UserToken user = objectMapper.readValue(response.getBody(), UserToken.class);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("token", user.getToken());
                session.setAttribute("loginSuccess", true);
                return "redirect:/kalender?month=0";
            } else {
                session.setAttribute("loginError", "Ungültiger Benutzername oder Passwort");
                session.setAttribute("benutzername", loginData.getUsername());
                return "redirect:/login";
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String showLogoutView(Model model, HttpSession session) {
        Boolean logedIn = (Boolean) session.getAttribute("loginSuccess");

        if (logedIn != null && logedIn) {
            model.addAttribute("loggedIn", false);
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/kalender")
    public String showKalenderView(@RequestParam(name = "month", required = false) int parameter, Model model,
            HttpSession session) {
        String[] dayStrings = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
        ArrayList<String> days = new ArrayList<>(Arrays.asList(dayStrings));

        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");
        if (loggedIn != null && loggedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            session.setAttribute("token", token);
            model.addAttribute("kalender", true);
            model.addAttribute("username", username);
        }

        if (parameter == 0) {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now());
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", calendarMonth.getMonthYear());
            model.addAttribute("nextMonthUrl", "kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "kalender?month=" + (parameter - 1));
        } else {
            CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now().plusMonths(parameter));
            model.addAttribute("kalenderTage", days);
            model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
            model.addAttribute("title", calendarMonth.getMonthYear());
            model.addAttribute("nextMonthUrl", "kalender?month=" + (parameter + 1));
            model.addAttribute("previousMonthUrl", "kalender?month=" + (parameter - 1));
        }

        return "index";
    }

    @GetMapping("/monatsuebersicht")
    public String showMonatsuebersichtView(Model model, HttpSession session) {
        Boolean logedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");

        if (logedIn != null && logedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            session.setAttribute("token", token);
            model.addAttribute("username", username);
            model.addAttribute("monatsuebersicht", true);
            model.addAttribute("title", "Monatsübersicht");
        }
        return "index";
    }

    public ResponseEntity<String> sendPostRequestToOtherService(String url, String body) {
        RestTemplate restTemplate = new RestTemplate();

        // Annahme: Du möchtest Daten im JSON-Format senden
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        // Führe die POST-Anfrage durch
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response;
    }
}
