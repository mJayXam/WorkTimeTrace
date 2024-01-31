package com.worktimetrace.timemanagement;

import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.timemanagement.Database.Hours;
import com.worktimetrace.timemanagement.Security.SecurityManager;
import com.worktimetrace.timemanagement.Security.User;
import com.worktimetrace.timemanagement.Database.HourRepo;
import com.worktimetrace.timemanagement.Database.HourSender;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class AccessController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SecurityManager sec;

    @Autowired
    HourRepo rep;

    @PostMapping("/all")
    public ResponseEntity<ArrayList<Hours>> findAll(@RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        logger.info("FIND ALL");
        if (!sec.wrongToken(username, token.substring("Bearer ".length())).getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        ArrayList<Hours> ret = new ArrayList<Hours>();
        rep.findAll().forEach(ret::add);
        logger.info("findAll OK");
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/byID/{param}")
    public ResponseEntity<Hours> findById(@PathVariable Long param, @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        logger.info("FIND BY ID");
        if (!sec.wrongToken(username, token.substring("Bearer ".length())).getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        Optional<Hours> request = rep.findById(param);
        if (request.isPresent()) {
            logger.info("findById OK");
            return ResponseEntity.ok(request.get());
        } else
            return null;
    }

    @PostMapping("/insertOne")
    public ResponseEntity<String> insertOne(@RequestBody HourSender entity, @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        logger.info("INSERT ONE");
        if (!sec.wrongToken(username, token.substring("Bearer ".length())).getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        rep.save(new Hours(entity));
        logger.info("insertOne OK");
        return ResponseEntity.ok("Speichern Erfolgreich");
    }

    @PostMapping("/insertMany")
    public ResponseEntity<String> insertMany(@RequestBody List<HourSender> entity,
            @RequestHeader("username") String username, @RequestHeader("Authorization") String token) {
        logger.info("INSERT MANY");
        if (!sec.wrongToken(username, token.substring("Bearer ".length())).getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        var entrylist = new ArrayList<Hours>();
        entity.stream().forEach((e) -> entrylist.add(new Hours(e)));
        rep.saveAll(entrylist);
        logger.info("insertMany OK");
        return ResponseEntity.ok("Spechern Erfolgreich");
    }

    @GetMapping("/byNID")
    public ResponseEntity<?> findByNID(
            @RequestHeader("username") String username, @RequestHeader("Authorization") String token) {
        logger.info("FIND BY NID");
        ResponseEntity<User> user = sec.wrongToken(username, token.substring("Bearer ".length()));
        if (!user.getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        ArrayList<Hours> ret = new ArrayList<>();
        rep.findByUserid(user.getBody().getId()).forEach(ret::add);
        logger.info("findByNID OK");
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/byNIDforMonth/{month}")
    public ResponseEntity<ArrayList<Hours>> findByNIDfilterByMonth(
            @PathVariable String month,
            @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        logger.info("FIND BY NID FILTER BY MONTH");
        var user = sec.wrongToken(username, token.substring("Bearer ".length()));
        if (!user.getStatusCode()
                .is2xxSuccessful()) {
            logger.info("Unauthorized");
            return ResponseEntity.status(401).build();
        }
        ArrayList<Hours> ret = new ArrayList<>();
        Date compD = Date.valueOf(month + "-01");
        rep.findByUserid(user.getBody().getId()).stream()
                .filter(e -> ((e.getDate().toLocalDate().getMonth() == compD.toLocalDate().getMonth())
                        && (e.getDate().toLocalDate().getYear() == compD.toLocalDate().getYear())))
                .forEach(ret::add);
        logger.info("findByNIDfilterByMonth OK");
        return ResponseEntity.ok(ret);
    }

}
