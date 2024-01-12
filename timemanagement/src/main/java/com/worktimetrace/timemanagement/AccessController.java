package com.worktimetrace.timemanagement;

import org.springframework.web.bind.annotation.RestController;

import com.worktimetrace.timemanagement.Database.Stunden;
import com.worktimetrace.timemanagement.Database.StundenRepo;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class AccessController {

    @Autowired
    StundenRepo rep;
    
    @GetMapping("/all")
    public ArrayList<Stunden> findAll() {
        ArrayList<Stunden> ret = new ArrayList<Stunden>();
        rep.findAll().forEach(ret::add);
        return ret;
    }

    @GetMapping("/byID/{param}")
    public Stunden getMethodName(@PathVariable Long param) {
        Optional<Stunden> request = rep.findById(param);
        if (request.isPresent())
            return request.get();
        else
            return null;
    }
    

}
