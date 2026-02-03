package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.adminStats.AdminStatsResponse;
import com.utn.eventmanager.service.adminStats.AdminStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final AdminStatsService statsService;

    public AdminStatsController(AdminStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public AdminStatsResponse getStats() {
        return statsService.getStats();
    }
}