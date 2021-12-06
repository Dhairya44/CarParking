package com.smart.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.MonthDay;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "PARKING")
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String location;
    private int available;
    private int waiting;
    private String nameOfUsers;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String day;
    private String inTime;
    private String outTime;
    private String service;
    private Date date;
    private int price;
    private String regisNumber;
    private int workerRating;
    public int getPrice() {
        return price;
    }

    public int getWorkerRating() {
        return workerRating;
    }

    public void setWorkerRating(int workerRating) {
        this.workerRating = workerRating;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRegisNumber() {
        return regisNumber;
    }

    public void setRegisNumber(String regisNumber) {
        this.regisNumber = regisNumber;
    }

    public ParkingSlot() {
        super();
        waiting = 0;
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

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

