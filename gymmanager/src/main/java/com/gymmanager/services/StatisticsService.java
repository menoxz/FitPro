package com.gymmanager.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.model.Subscription;
import com.gymmanager.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final SubscriptionRepository subscriptionRepository;

    public long getActiveCustomersCount() {
        return subscriptionRepository.countActiveCustomers();
    }

    public BigDecimal getEstimatedMonthlyRevenue() {
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();
        return subscriptionRepository.calculateMonthlyRevenue(currentMonth, currentYear);
    }

    public List<SubscriptionDto.Response> getSubscriptionsForPeriod(LocalDate startDate, LocalDate endDate) {
        return subscriptionRepository.findSubscriptionsInPeriod(startDate, endDate)
            .stream()
            .map(SubscriptionDto.Response::fromEntity)
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getRevenueByMonth() {
        List<Map<String, Object>> revenueByMonth = subscriptionRepository.getRevenueByMonth();
        return revenueByMonth;
    }

    public Map<String, Long> getCustomersCountByPack() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByActiveTrue();
        Map<String, Long> customersCountByPack = new HashMap<>();

        for (Subscription subscription : activeSubscriptions) {
            String packName = subscription.getPack().getName();
            customersCountByPack.put(packName, customersCountByPack.getOrDefault(packName, 0L) + 1);
        }

        return customersCountByPack;
    }
}
