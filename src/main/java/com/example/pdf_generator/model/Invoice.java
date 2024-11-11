package com.example.pdf_generator.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class Invoice {
    @NotBlank
    private String seller;
    @NotBlank
    private String sellerGstin;
    @NotBlank
    private String sellerAddress;
    @NotBlank
    private String buyer;
    @NotBlank
    private String buyerGstin;
    @NotBlank
    private String buyerAddress;
    @NotEmpty
    private List<Item> items;

    // Getters and setters
    // Constructor

    public Invoice() {
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerGstin() {
        return sellerGstin;
    }

    public void setSellerGstin(String sellerGstin) {
        this.sellerGstin = sellerGstin;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerGstin() {
        return buyerGstin;
    }

    public void setBuyerGstin(String buyerGstin) {
        this.buyerGstin = buyerGstin;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "seller='" + seller + '\'' +
                ", sellerGstin='" + sellerGstin + '\'' +
                ", sellerAddress='" + sellerAddress + '\'' +
                ", buyer='" + buyer + '\'' +
                ", buyerGstin='" + buyerGstin + '\'' +
                ", buyerAddress='" + buyerAddress + '\'' +
                ", items=" + items +
                '}';
    }
}