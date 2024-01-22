package com.worktimetrace.timemanagement;

import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.timemanagement.Database.Hours;
import com.worktimetrace.timemanagement.Database.HourRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class AccessController {

    @Autowired
    HourRepo rep;
    
    @GetMapping("/all")
    public ResponseEntity<ArrayList<Hours>> findAll() {
        ArrayList<Hours> ret = new ArrayList<Hours>();
        rep.findAll().forEach(ret::add);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/byID/{param}")
    public ResponseEntity<Hours> findById(@PathVariable Long param) {
        Optional<Hours> request = rep.findById(param);
        if (request.isPresent())
            return ResponseEntity.ok(request.get());
        else
            return null;
    }
    
    @PostMapping("/insertOne")
    public ResponseEntity<String> insertOne(@RequestBody Hours entity) {
        rep.save(entity);
        return ResponseEntity.ok("Speichern Erfolgreich");
    }
    
    @PostMapping("/insertMany")
    public ResponseEntity<String> insertMany(@RequestBody List<Hours> entity) {
        rep.saveAll(entity);
        return ResponseEntity.ok("Spechern Erfolgreich");
    }
    
    @GetMapping("/byNID/{param}")
    public ResponseEntity<ArrayList<Hours>> findByNID(@PathVariable Long param) {
        ArrayList<Hours> ret = new ArrayList<>();
        rep.findByUserid(param).forEach(ret::add);
        return ResponseEntity.ok(ret);
    }
    
}
