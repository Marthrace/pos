package com.checkout.checkout_system.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReceiptDTO {

    private Long orderId;
    private LocalDateTime date;

    private List<ReceiptItemDTO> items;

    private double total;
    private double paid;
    private double change;
    private String paymentMethod;

    public ReceiptDTO() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ReceiptItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ReceiptItemDTO> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}