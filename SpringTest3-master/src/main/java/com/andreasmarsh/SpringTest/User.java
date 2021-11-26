package com.andreasmarsh.SpringTest;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @Column(nullable = false, unique = true, updatable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 64)
    private String password;

    private boolean adminStatus;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    private boolean promotions;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "address_addressID", nullable = true, referencedColumnName = "addressID")
    @NotFound(action = NotFoundAction.IGNORE)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<CreditCard> creditCards = new ArrayList<>();

    public void addCreditCard(String cardType, Long cardNumber, Long expMonth, Long expYear, Long cvv){
        this.creditCards.add(new CreditCard(cardType, cardNumber, expMonth, expYear, cvv, this));
    }

    public Long getId() {
        return userID;
    }

    public void setId(Long userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //setter & getter for admin status
    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    public boolean getAdminStatus() {return adminStatus;}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public void setCreditCard(Long id, String cardType, Long cardNumber, Long expMonth, Long expYear, Long cvv) {
        this.creditCards.add(new CreditCard(id, cardType, cardNumber, expMonth, expYear, cvv, this));
    }

    public void addCreditCard(CreditCard creditCard) {
        creditCards.add(creditCard);
    }

    public void removeCreditCard(CreditCard creditCard) {
        creditCards.remove(creditCard);
    }

    public boolean isPromotions() {
        return promotions;
    }

    public void setPromotions(boolean promotions) {
        this.promotions = promotions;
    }

    public User get() {
        return this;
    }
}