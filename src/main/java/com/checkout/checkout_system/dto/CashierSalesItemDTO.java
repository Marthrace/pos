package com.checkout.checkout_system.dto;

public class CashierSalesItemDTO {

    private String productName;
    private int quantitySold;
    private double total;

    public CashierSalesItemDTO() {}

    public CashierSalesItemDTO(String productName, int quantitySold, double total) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}