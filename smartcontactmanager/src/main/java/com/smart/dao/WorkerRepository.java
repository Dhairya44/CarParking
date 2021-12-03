package com.smart.dao;


import java.util.List;

import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Worker;
import com.smart.entities.User;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    public List<Worker> findWorkerByName(@Param("name")String name);
   // public static Worker getWorkerByUserName(@Param("email") String email);
}
