package com.checkout.checkout_system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.checkout_system.dto.CashierSalesReportDTO;
import com.checkout.checkout_system.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Existing summary endpoint
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return reportService.getSummary();
    }

    // ✅ New endpoint for cashier sales report
    @GetMapping("/cashier/{username}")
public CashierSalesReportDTO getCashierReport(
        @PathVariable String username) {
    return reportService.getSalesForCashier(username);
}
}