package com.gymmanager.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymmanager.dto.CustomerDto;
import com.gymmanager.dto.PackDto;
import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.model.Customer;
import com.gymmanager.model.Pack;
import com.gymmanager.model.Subscription;
import com.gymmanager.repository.SubscriptionRepository;
import com.gymmanager.security.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.gymmanager.security.exception.GlobalExceptionHandler.SubscriptionAlreadyExistException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final CustomerService customerService;
    private final PackService packService;

    @Transactional
    public SubscriptionDto.Response create(SubscriptionDto.Create request) {
        Customer customer = customerService.getById(request.getCustomerId());
        Pack pack = packService.getById(request.getPackId());

    
        //verifier s'il y a un abonnement actif pour ce customer
        List<Subscription> activeSubscriptions = subscriptionRepository.findActiveSubByCustomerId(customer.getId());
        if (!activeSubscriptions.isEmpty()) {
            throw new SubscriptionAlreadyExistException("Un abonnement actif existe déjà pour ce client");
        }else{
            Subscription subscription = new Subscription();
            subscription.setCustomer(customer);
            subscription.setPack(pack);
            subscription.setStartDate(LocalDate.now());
            subscription.setActive(true);
            subscription.setMonthlyPrice(pack.getMonthlyPrice());

        return toDto(subscriptionRepository.save(subscription));
        }

        // // Désactiver les abonnements existants
        // subscriptionRepository.deactivateAllActiveSubscriptions(customer.getId());

        
    }

    

    @Transactional
    public void cancel(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Abonnement non trouvé"));
        
        subscription.setActive(false);
        subscriptionRepository.save(subscription);
    }

    public List<SubscriptionDto.Response> getCustomerSubscriptions(Long customerId) {
        return subscriptionRepository.findByCustomerId(customerId)
            .stream()
            .map(this::toDto)
            .toList();
    }
    public List<SubscriptionDto.Response> getAll() {
        return subscriptionRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto.Response> getAllPage() {
        return subscriptionRepository.findAll().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SubscriptionDto.Response> search(String query, Pageable pageable) {
        return subscriptionRepository.searchPage(query, pageable)
            .map(this::toDto);
    }

    private SubscriptionDto.Response toDto(Subscription subscription) {
        return SubscriptionDto.Response.builder()
            .id(subscription.getId())
            .startDate(subscription.getStartDate())
            .active(subscription.isActive())
            .customer(CustomerDto.Response.fromEntity(subscription.getCustomer()))
            .pack(PackDto.Response.fromEntity(subscription.getPack()))
            .build();
    }
    
    public boolean isSubscriptionActive(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow(() -> new ResourceNotFoundException("Abonnement non trouvé"));
        
        return subscription.isActive();
    }
    
}
