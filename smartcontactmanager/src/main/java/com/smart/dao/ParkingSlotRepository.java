package com.smart.dao;

import com.smart.entities.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Integer> {
    public List<ParkingSlot> findParkingSlotByNameOfUsersContaining(@Param("nameOfUsers") String nameOfUsers);
    public List<ParkingSlot> findParkingSlotByServiceContaining(@Param("name") String name);
}
