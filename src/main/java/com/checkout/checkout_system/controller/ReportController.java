package com.checkout.checkout_system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.checkout_system.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(
            ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return reportService.getSummary();
    }
}