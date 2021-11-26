package com.andreasmarsh.SpringTest;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressID;

    @OneToOne (mappedBy = "address")
    private User user;

    @Column(name = "street")
    private String street;

    @Column(name = "appartment_Number")
    private String appartmentNumber;

    @Column(name = "city")
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    public Long getAddressID() {
        return addressID;
    }

    @Column(name = "zip_Code", length = 5)
    private Long zipCode;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAppartmentNumber() {
        return appartmentNumber;
    }

    public void setAppartmentNumber(String appartmentNumber) {
        this.appartmentNumber = appartmentNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }
}