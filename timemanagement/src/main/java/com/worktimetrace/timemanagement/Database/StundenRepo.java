package com.worktimetrace.timemanagement.Database;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface StundenRepo extends CrudRepository<Stunden, Long> {
    List<Stunden> findByNutzerid(Long nutzerid);
}
