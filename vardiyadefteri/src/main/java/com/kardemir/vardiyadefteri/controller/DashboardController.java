package com.kardemir.vardiyadefteri.controller;

import com.kardemir.vardiyadefteri.service.DashboardService;
import com.kardemir.vardiyadefteri.dto.DashboardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardDTO getDashboardData() {
        return dashboardService.getDashboardInfo();
    }
}
