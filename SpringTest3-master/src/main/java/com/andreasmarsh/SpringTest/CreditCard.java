package com.andreasmarsh.SpringTest;

import javax.persistence.*;

@Entity
@Table(name = "creditcard")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "cardType", length = 45)
    private String cardType;

    @Column(name = "cardNumber", length = 16)
    private Long cardNumber;

    @Column(name = "expMonth", length = 2)
    private Long expMonth;

    @Column(name = "expYear", length = 2)
    private Long expYear;

    @Column(name = "cvv", length = 3)
    private Long cvv;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard )) return false;
        return cardID != null && cardID.equals(((CreditCard) o).getCardID());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public CreditCard() {

    }

    public CreditCard(Long id, String cardType, Long cardNumber, Long expMonth, Long expYear, Long cvv, User user) {
        this.cardID = id;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.user = user;
    }

    public CreditCard(String cardType, Long cardNumber, Long expMonth, Long expYear, Long cvv, User user) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvv = cvv;
        this.user = user;
    }

    public Long getCardID() {
        return cardID;
    }

    public void setCardID(Long cardID) {
        this.cardID = cardID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(Long expMonth) {
        this.expMonth = expMonth;
    }

    public Long getExpYear() {
        return expYear;
    }

    public void setExpYear(Long expYear) {
        this.expYear = expYear;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }
}