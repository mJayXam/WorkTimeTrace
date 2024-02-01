package com.worktimetrace.frontend.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import com.worktimetrace.frontend.Models.HourDate;
import com.worktimetrace.frontend.Models.HourSender;
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
        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        if (loggedIn != null && loggedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            return "redirect:/Kalender?month=0";
        } else {
            session.invalidate();
        }
        model.addAttribute("landingPage", true);
        return "index";
    }

    @GetMapping("/Registration")
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

    @PostMapping("/Registration")
    public String getRegistrationData(@ModelAttribute("user") User user, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJsonRegistration = objectMapper.writeValueAsString(user);
            ResponseEntity<String> responseRegistration = sendPostRequestToOtherService("https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/register", requestJsonRegistration);

            HttpStatusCode httpStatusCodeRegistration = responseRegistration.getStatusCode();
            if (httpStatusCodeRegistration == HttpStatus.OK) {
                LoginData loginData = new LoginData(user.getUsername(), user.getPassword());
                String requestJsonLogin = objectMapper.writeValueAsString(loginData);
                ResponseEntity<String> responseLogin = sendPostRequestToOtherService("https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/login", requestJsonLogin);
                HttpStatusCode httpStatusCodeLogin = responseLogin.getStatusCode();
                if (httpStatusCodeLogin == HttpStatus.OK) {
                    UserToken userToken = objectMapper.readValue(responseLogin.getBody(), UserToken.class);
                    session.setAttribute("username", userToken.getUsername());
                    session.setAttribute("token", userToken.getToken());
                    session.setAttribute("loginSuccess", true);

                    return "redirect:/Kalender?month=0";
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
            return "redirect:/Registration";
        } catch (JsonProcessingException e) {
            return "redirect:/Registration";
        }

        return "redirect:/";
    }

    @GetMapping("/Login")
    public String showLoginView(Model model, HttpSession session) {
        LoginData loginData = new LoginData();
        String loginError = (String) session.getAttribute("loginError");

        if (loginError != null) {
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

    @PostMapping("/Login")
    public String getLoginData(@ModelAttribute("logindata") LoginData loginData, HttpSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(loginData);
            ResponseEntity<String> response = sendPostRequestToOtherService("https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/auth/login", requestJson);

            HttpStatusCode httpStatusCode = response.getStatusCode();

            if (httpStatusCode == HttpStatus.OK) {
                UserToken user = objectMapper.readValue(response.getBody(), UserToken.class);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("token", user.getToken());
                session.setAttribute("loginSuccess", true);
                return "redirect:/Kalender?month=0";
            } else {
                session.setAttribute("loginError", "Ungültiger Benutzername oder Passwort");
                session.setAttribute("benutzername", loginData.getUsername());
                return "redirect:/Login";
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

    @GetMapping("/Logout")
    public String showLogoutView(Model model, HttpSession session) {
        Boolean logedIn = (Boolean) session.getAttribute("loginSuccess");

        if (logedIn != null && logedIn) {
            model.addAttribute("loggedIn", false);
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/Kalender")
    public String showCalendarView(@RequestParam(name = "month", required = true) int month, Model model,
            HttpSession session) {
        String[] dayStrings = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };
        ArrayList<String> days = new ArrayList<>(Arrays.asList(dayStrings));

        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");
        System.out.println(token);


        if (loggedIn != null && loggedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            session.setAttribute("token", token);
            model.addAttribute("kalender", true);
            model.addAttribute("username", username);
            if (month == 0) {
                ArrayList<HourSender> hourSenders = getPriviousAndNextMonthByDate(LocalDate.now(), username, token);
                CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now(), hourSenders);
                model.addAttribute("kalenderTage", days);
                model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
                model.addAttribute("title", calendarMonth.getMonthYear());
                model.addAttribute("nextMonthUrl", "Kalender?month=" + (month + 1));
                model.addAttribute("previousMonthUrl", "Kalender?month=" + (month - 1));
            } else {
                ArrayList<HourSender> hourSenders = getPriviousAndNextMonthByDate(LocalDate.now().plusMonths(month), username, token);
                CalendarMonth calendarMonth = new CalendarMonth(LocalDate.now().plusMonths(month), hourSenders);
                model.addAttribute("kalenderTage", days);
                model.addAttribute("datum", calendarMonth.getWeeksAndDaysInMonth());
                model.addAttribute("title", calendarMonth.getMonthYear());
                model.addAttribute("nextMonthUrl", "Kalender?month=" + (month + 1));
                model.addAttribute("previousMonthUrl", "Kalender?month=" + (month - 1));
            }
            return "index";
        } else {
            session.invalidate();
            return "redirect:/";
        }
    }

    @GetMapping("/KalenderTag")
    public String showCalendarDayView(@RequestParam(name = "day", required = true) int day,
            @RequestParam(name = "month", required = true) int month,
            @RequestParam(name = "year", required = true) int year, Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");

        LocalDate datum = LocalDate.of(year, month, day);

        if (loggedIn != null && loggedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            session.setAttribute("token", token);
            model.addAttribute("datum", datum);
            model.addAttribute("username", username);
            model.addAttribute("kalenderTag", true);
            model.addAttribute("hourDate", new HourDate(datum.toString(), 0));
            model.addAttribute("title", "Stunden buchen");
        } else {
            session.invalidate();
            return "redirect:/";
        }

        return "index";
    }

    @PostMapping("/KalenderTag")
    public String postCalendarDay(@ModelAttribute("hour") HourDate hour, Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");
        System.out.println(hour.toString());

        User user = getUserInfo(username, token);

        if (user != null) {
            ResponseEntity<String> response = insertOneHourEntry(hour, user, username, token);
            if (response != null) {
                if (loggedIn != null && loggedIn) {
                    model.addAttribute("loggedIn", true);
                    session.setAttribute("loginSuccess", true);
                    session.setAttribute("token", token);
                    model.addAttribute("username", username);
                    return "redirect:/Kalender?month=0";
                } else {
                    session.invalidate();
                    return "redirect:/";
                }
            }
        }

        session.invalidate();
        return "redirect:/";

    }

    @GetMapping("/Monatsuebersicht")
    public String showMonatsuebersichtView(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");

        if (loggedIn != null && loggedIn) {
            model.addAttribute("loggedIn", true);
            session.setAttribute("loginSuccess", true);
            session.setAttribute("token", token);
            model.addAttribute("username", username);
            model.addAttribute("monatsuebersicht", true);
            model.addAttribute("title", "Monatsübersicht");
        } else {
            session.invalidate();
            return "redirect:/";
        }

        return "index";
    }

    @GetMapping("/Benutzerinformationen")
    public String showBenutzerinformationenView(Model model, HttpSession session) {
        Boolean loggedIn = (Boolean) session.getAttribute("loginSuccess");
        String username = (String) session.getAttribute("username");
        String token = (String) session.getAttribute("token");

        if (loggedIn != null && loggedIn) {
            User user = getUserInfo(username, token);
            if (user != null) {
                model.addAttribute("loggedIn", true);
                session.setAttribute("loginSuccess", true);
                session.setAttribute("token", token);
                model.addAttribute("username", username);
                model.addAttribute("benutzerinformationen", true);
                model.addAttribute("user", user);
                model.addAttribute("title", "Benutzerinformationen");
            }
        } else {
            session.invalidate();
            return "redirect:/";
        }
        return "index";
    }

    private ResponseEntity<String> sendPostRequestToOtherService(String url, String body) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        return response;
    }

    private User getUserInfo(String username, String token) {
        RestTemplate rt = new RestTemplate();
        String url = "https://usermanagementservice-dev-5rt6jcn4da-uc.a.run.app/user/info";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", username);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<User> responseEntity = rt.exchange(url, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<User>() {});

            HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

            if (httpStatusCode == HttpStatus.OK) {
                User user = responseEntity.getBody();
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private ResponseEntity<String> insertOneHourEntry(HourDate hour, User user, String username, String token) {
        HourSender hourSender = new HourSender(hour.getHour(), hour.getDate(), user.getId());
        String url = "https://timemanagementservice-dev-5rt6jcn4da-uc.a.run.app/insertOne";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.add("username", username);
        header.set("Authorization", "Bearer " + token);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestJson = objectMapper.writeValueAsString(hourSender);
            System.out.println(requestJson);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, header);

            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            HttpStatusCode httpStatusCode = response.getStatusCode();
            if (httpStatusCode == HttpStatus.OK) {
                return response;
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private List<HourSender> getAllKalenderEntrysOfUser(String username, String token) {
        RestTemplate rt = new RestTemplate();
        String url = "https://timemanagementservice-dev-5rt6jcn4da-uc.a.run.app/byNID";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", username);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, requestEntity, String.class);

            HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

            if (httpStatusCode == HttpStatus.OK) {
                String jsonResponse = responseEntity.getBody();
                
                ObjectMapper objectMapper = new ObjectMapper();
                HourSender[] hourSenders = objectMapper.readValue(jsonResponse, HourSender[].class);
                return Arrays.asList(hourSenders);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }*/

    private List<HourSender> getAllKalenderEntrysOfUserInMonth(String username, String token, String monthYear) {
        RestTemplate rt = new RestTemplate();
        String url = "https://timemanagementservice-dev-5rt6jcn4da-uc.a.run.app/byNIDforMonth/" + monthYear;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("username", username);
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, requestEntity, String.class);

            HttpStatusCode httpStatusCode = responseEntity.getStatusCode();

            if (httpStatusCode == HttpStatus.OK) {
                String jsonResponse = responseEntity.getBody();
                if(jsonResponse != null) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    HourSender[] hourSenders = objectMapper.readValue(jsonResponse, HourSender[].class);
                    return Arrays.asList(hourSenders);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<HourSender> getPriviousAndNextMonthByDate(LocalDate date, String username, String token) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.GERMAN);
        LocalDate thisMonthDate = date;
        LocalDate nextMonthDate = date.plusMonths(1);
        LocalDate previousMonthDate = date.minusMonths(1);

        String monthYearThisMonth = thisMonthDate.format(formatter);
        String monthYearPreviousMonth = nextMonthDate.format(formatter);
        String monthYearNextMonth = previousMonthDate.format(formatter);

        List<HourSender> thisMonth = getAllKalenderEntrysOfUserInMonth(username, token, monthYearThisMonth);
        List<HourSender> previousMonth = getAllKalenderEntrysOfUserInMonth(username, token, monthYearPreviousMonth);
        List<HourSender> nextMonth = getAllKalenderEntrysOfUserInMonth(username, token, monthYearNextMonth);

        if (thisMonth != null || nextMonth != null || previousMonth != null) {

            ArrayList<HourSender> returnList = new ArrayList<>();

            if(thisMonth != null) {
                returnList.addAll(new ArrayList<>(thisMonth));
            }
            if(nextMonth != null) {
                returnList.addAll(new ArrayList<>(nextMonth));
            }
            if(previousMonth != null) {
                returnList.addAll(new ArrayList<>(previousMonth));
            }

            Collections.sort(returnList, Comparator.comparing(HourSender::getDate));
            return returnList;
        } else {
            return null;
        }
    }
}
