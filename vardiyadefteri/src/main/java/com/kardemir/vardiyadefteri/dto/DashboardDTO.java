package com.kardemir.vardiyadefteri.dto;

import lombok.Data;

@Data
public class DashboardDTO {
    private long totalUsers;
    private long activeUsers;
    private long totalShifts;
    private long todayShifts;
}
