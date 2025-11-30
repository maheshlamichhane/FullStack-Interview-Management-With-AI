package com.itsutra.project.dao;

import com.itsutra.project.entity.DocumentAccessLog;
import org.springframework.data.repository.CrudRepository;

public interface DocumentAccessLogDAO extends CrudRepository<DocumentAccessLog, Long> {
}
