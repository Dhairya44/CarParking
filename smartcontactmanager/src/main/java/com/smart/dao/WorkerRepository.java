package com.smart.dao;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.entities.Worker;
import com.smart.entities.User;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {
	//pagination...
	
	@Query("from Worker as c where c.user.id =:userId")
	//currentPage-page
	//Contact Per page - 5
	public Page<Worker> findWorkersByUser(@Param("userId")int userId, Pageable pePageable);
	
	//search
	public List<Worker> findByNameContainingAndUser(String name,User user);
	
}
