package com.fx.analyzeFx;

import jakarta.persistence.*;
import java.util.Currency;
import java.util.Date;

@Entity
public class FxTransaction {
    @Id
    @Column(nullable = false)
    private Integer transactionId;

    @Column(nullable = false)
    private Currency currencyFrm;

    @Column(nullable = false)
    private Currency currencyTo;

    @Column(nullable = false)
    private Date dealDate;

    @Column(nullable = false)
    private Double dealAmount;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Currency getCurrencyFrm() {
        return currencyFrm;
    }

    public void setCurrencyFrm(Currency currencyFrm) {
        this.currencyFrm = currencyFrm;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    public Double getDealAmount() {
        return dealAmount;
    }

    public void setDealAmount(Double dealAmount) {
        this.dealAmount = dealAmount;
    }
}
