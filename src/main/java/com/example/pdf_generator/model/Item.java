package com.example.pdf_generator.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Item {
    @NotBlank
    private String name;
    @NotBlank
    private String quantity;
    @NotNull
    private BigDecimal rate;
    @NotNull
    private BigDecimal amount;

    // Getters and setters
    // Constructor


    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rate=" + rate +
                ", amount=" + amount +
                '}';
    }
}
