package com.smart.entities;

import javax.persistence.*;

@Entity
@Table(name = "PARKING")
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String location;
    private int available;
    private String nameOfUsers;
    private String inTime;
    private String outTime;
    public ParkingSlot() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getNameOfUsers() {
        return nameOfUsers;
    }

    public void setNameOfUsers(String nameOfUsers) {
        this.nameOfUsers = nameOfUsers;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}

