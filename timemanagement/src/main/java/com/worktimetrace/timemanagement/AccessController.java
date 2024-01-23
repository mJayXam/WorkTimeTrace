package com.worktimetrace.timemanagement;

import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.timemanagement.Database.Stunden;
import com.worktimetrace.timemanagement.Database.StundenRepo;

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
    StundenRepo rep;
    
    @GetMapping("/all")
    public ResponseEntity<ArrayList<Stunden>> findAll() {
        ArrayList<Stunden> ret = new ArrayList<Stunden>();
        rep.findAll().forEach(ret::add);
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/byID/{param}")
    public ResponseEntity<Stunden> findById(@PathVariable Long param) {
        Optional<Stunden> request = rep.findById(param);
        if (request.isPresent())
            return ResponseEntity.ok(request.get());
        else
            return null;
    }
    
    @PostMapping("/insertOne")
    public ResponseEntity<String> insertOne(@RequestBody Stunden entity) {
        rep.save(entity);
        return ResponseEntity.ok("Speichern Erfolgreich");
    }
    
    @PostMapping("/insertMany")
    public ResponseEntity<String> insertMany(@RequestBody List<Stunden> entity) {
        rep.saveAll(entity);
        return ResponseEntity.ok("Spechern Erfolgreich");
    }
    
    @GetMapping("/byNID/{param}")
    public ResponseEntity<ArrayList<Stunden>> findByNID(@PathVariable Long param) {
        ArrayList<Stunden> ret = new ArrayList<>();
        rep.findByNutzerid(param).forEach(ret::add);
        return ResponseEntity.ok(ret);
    }
    
}
