package com.gymmanager.controller;

import com.gymmanager.dto.CustomerDto;
import com.gymmanager.dto.PackDto;
import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.model.Customer;
import com.gymmanager.model.Subscription;
// import com.gymmanager.repository.SubscriptionRepository;
import com.gymmanager.services.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class CustomerController {

    private final CustomerService customerService;
    // private final SubscriptionRepository subscriptionRepository;

    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public Page<CustomerDto.Response> getAll(Pageable pageable) {
        return customerService.getAll(pageable);
    }
    @GetMapping("/all")
    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public List<CustomerDto.Response> getAllcustomer() {
        return customerService.getAllcustomer();
    }

    @GetMapping("/search")
    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public Page<CustomerDto.Response> search(
        @RequestParam String query, 
        Pageable pageable
    ) {
        return customerService.search(query, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // @PreAuthorize("hasRole('ADMIN')")
    public CustomerDto.Response create(@Valid @RequestBody CustomerDto.Request request) {
        return customerService.create(request);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public CustomerDto.Response update(
        @PathVariable Long id,
        @Valid @RequestBody CustomerDto.Request request
    ) {
        return customerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        customerService.delete(id);
    }

    @GetMapping("/{id}")
    public CustomerDto.Response getById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerWithSubscriptions(id);
        
        return CustomerDto.Response.builder()
            .id(customer.getId())
            .fullName(customer.getFirstName() + " " + customer.getLastName())
            .phone(customer.getPhone())
            .registrationDate(customer.getRegistrationDate().toString())
            .activeSubscription(
                customer.getSubscriptions().stream()
                    .filter(Subscription::isActive)
                    .findFirst()
                    .map(this::toSubscriptionResponse)
                    .orElse(null)
            )
            .subscriptionCount(customer.getSubscriptions().size())
            .build();
    }

    private SubscriptionDto.Response toSubscriptionResponse(Subscription subscription) {
        return SubscriptionDto.Response.builder()
            .id(subscription.getId())
            .startDate(subscription.getStartDate())
            .active(subscription.isActive())
            .customer(CustomerDto.Response.builder() // Version simplifiée
                .id(subscription.getCustomer().getId())
                .fullName(subscription.getCustomer().getFirstName()+ " " +subscription.getCustomer().getLastName())
                .build())
            .pack(PackDto.Response.fromEntity(subscription.getPack()))
            .build();
    }



}
