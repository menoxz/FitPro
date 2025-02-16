package com.gymmanager.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymmanager.model.Subscription;
import com.gymmanager.repository.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionExpiryService {
    
    private final SubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 0 0 * * *") // Tous les jours à minuit
    @Transactional
    public void deactivateExpiredSubscriptions() {
        List<Subscription> activeSubscriptions = subscriptionRepository.findByActiveTrue();
        
        activeSubscriptions.stream()
            .filter(sub -> LocalDate.now().isAfter(sub.calculateEndDate()))
            .forEach(sub -> sub.setActive(false));
    }
}
