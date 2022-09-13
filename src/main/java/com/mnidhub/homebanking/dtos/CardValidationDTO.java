package com.mnidhub.homebanking.dtos;

import com.mnidhub.homebanking.models.Account;

import java.time.LocalDate;

public class CardValidationDTO {

    private int cvv;
    private String cardNumber;
    private String cardHolder;

    private Double amount;

    private String toAccountNumber;


    public CardValidationDTO() {
    }

    public CardValidationDTO(int cvv, String cardNumber, String cardHolder, Double amount, String toAccountNumber) {
        this.cvv = cvv;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.amount = amount;
        this.toAccountNumber = toAccountNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
}
