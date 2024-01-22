package com.worktimetrace.timemanagement.Database;

import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface HourRepo extends CrudRepository<Hours, Long> {
    List<Hours> findByUserid(Long userid);
}
