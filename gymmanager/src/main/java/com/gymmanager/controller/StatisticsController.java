package com.gymmanager.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.repository.SubscriptionRepository;
import com.gymmanager.services.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final SubscriptionRepository subscriptionRepository;

    @GetMapping("/active-customers")
    public ResponseEntity<Long> getActiveCustomersCount() {
        return ResponseEntity.ok(statisticsService.getActiveCustomersCount());
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<BigDecimal> getEstimatedMonthlyRevenue() {
        return ResponseEntity.ok(statisticsService.getEstimatedMonthlyRevenue());
    }

    public List<SubscriptionDto.Response> getSubscriptionsForPeriod(LocalDate startDate, LocalDate endDate) {
    return subscriptionRepository.findSubscriptionsInPeriod(startDate, endDate)
        .stream()
        .map(SubscriptionDto.Response::fromEntity)
        .collect(Collectors.toList());
    }

    @GetMapping("/revenue-by-month")
    public List<Map<String, Object>> getRevenueByMonth() {
        return statisticsService.getRevenueByMonth();
    }

    @GetMapping("/customers-count-by-pack")
    public Map<String, Long> getCustomersCountByPack() {
        return statisticsService.getCustomersCountByPack();
    }
}
