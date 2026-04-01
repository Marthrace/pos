package com.checkout.checkout_system.dto;

import java.util.List;

public class CashierSalesReportDTO {

    private String cashierUsername;
    private List<CashierSalesItemDTO> items;
    private int totalItemsSold;
    private double totalCash;

    public CashierSalesReportDTO() {}

    public CashierSalesReportDTO(String cashierUsername, List<CashierSalesItemDTO> items, int totalItemsSold, double totalCash) {
        this.cashierUsername = cashierUsername;
        this.items = items;
        this.totalItemsSold = totalItemsSold;
        this.totalCash = totalCash;
    }

    public String getCashierUsername() {
        return cashierUsername;
    }

    public void setCashierUsername(String cashierUsername) {
        this.cashierUsername = cashierUsername;
    }

    public List<CashierSalesItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CashierSalesItemDTO> items) {
        this.items = items;
    }

    public int getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(int totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }
}
