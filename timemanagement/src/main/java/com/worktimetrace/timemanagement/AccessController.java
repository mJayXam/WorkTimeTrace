package com.worktimetrace.timemanagement;

import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.timemanagement.Database.Hours;
import com.worktimetrace.timemanagement.Database.HourRepo;
import com.worktimetrace.timemanagement.Database.HourSender;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.worktimetrace.timemanagement.Security.SecurityManger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
public class AccessController {

    @Autowired
    HourRepo rep;

    @PostMapping("/all")
    public ResponseEntity<ArrayList<Hours>> findAll(@RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        ArrayList<Hours> ret = new ArrayList<Hours>();
        rep.findAll().forEach(ret::add);
        return ResponseEntity.ok(ret);
    }

    @PostMapping("/byID/{param}")
    public ResponseEntity<Hours> findById(@PathVariable Long param, @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        Optional<Hours> request = rep.findById(param);
        if (request.isPresent())
            return ResponseEntity.ok(request.get());
        else
            return null;
    }

    @PostMapping("/insertOne")
    public ResponseEntity<String> insertOne(@RequestBody HourSender entity, @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        rep.save(new Hours(entity));
        return ResponseEntity.ok("Speichern Erfolgreich");
    }

    @PostMapping("/insertMany")
    public ResponseEntity<String> insertMany(@RequestBody List<HourSender> entity,
            @RequestHeader("username") String username, @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        ;
        var entrylist = new ArrayList<Hours>();
        entity.stream().forEach((e) -> entrylist.add(new Hours(e)));
        rep.saveAll(entrylist);
        return ResponseEntity.ok("Spechern Erfolgreich");
    }

    @GetMapping("/byNID/{param}")
    public ResponseEntity<ArrayList<Hours>> findByNID(@PathVariable Long param,
            @RequestHeader("username") String username, @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        ArrayList<Hours> ret = new ArrayList<>();
        rep.findByUserid(param).forEach(ret::add);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/byNIDforMonth/{uid}/{month}")
    public ResponseEntity<ArrayList<Hours>> findByNIDfilterByMonth(@PathVariable Long uid,
            @PathVariable String month,
            @RequestHeader("username") String username,
            @RequestHeader("Authorization") String token) {
        if (!SecurityManger.wrongToken(username, token.substring("Bearer ".length())).getStatusCode().is2xxSuccessful())
            return ResponseEntity.status(401).build();
        ArrayList<Hours> ret = new ArrayList<>();
        Date compD = Date.valueOf(month + "-01");
        rep.findByUserid(uid).stream()
                .filter(e -> ((e.getDate().toLocalDate().getMonth() == compD.toLocalDate().getMonth())
                        && (e.getDate().toLocalDate().getYear() == compD.toLocalDate().getYear())))
                .forEach(ret::add);
        return ResponseEntity.ok(ret);
    }

}
