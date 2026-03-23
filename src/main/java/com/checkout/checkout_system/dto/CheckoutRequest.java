package com.checkout.checkout_system.dto;

import java.util.List;

public class CheckoutRequest {

    private List<CheckoutItem> items;

    // ✅ NEW
    private String paymentMethod;

    private double paidAmount;

    public CheckoutRequest() {
    }

    public List<CheckoutItem> getItems() {
        return items;
    }

    public void setItems(List<CheckoutItem> items) {
        this.items = items;
    }

    // ✅ getters setters

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }
}