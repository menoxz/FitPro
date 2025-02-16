package com.gymmanager.services;


import com.gymmanager.dto.CustomerDto;
import com.gymmanager.dto.PackDto;
import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.model.Customer;
import com.gymmanager.model.Subscription;
import com.gymmanager.repository.CustomerRepository;
import com.gymmanager.repository.SubscriptionRepository;
import com.gymmanager.security.exception.GlobalExceptionHandler.ActiveSubscriptionExistException;
import com.gymmanager.security.exception.GlobalExceptionHandler.NumberAlreadyExistException;
import com.gymmanager.security.exception.GlobalExceptionHandler.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Transactional(readOnly = true)
    public Page<CustomerDto.Response> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
            .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto.Response> getAllcustomer() {
        return customerRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Page<CustomerDto.Response> search(String query, Pageable pageable) {
        return customerRepository.searchByName(query, pageable)
            .map(this::toResponse);
    }

    @Transactional
    public CustomerDto.Response create(CustomerDto.Request request) {
        if(customerRepository.existsByPhone(request.getPhone())) {
            throw new NumberAlreadyExistException("This number is already exists");
        }

        Customer customer = Customer.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .build();

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDto.Response update(Long id, CustomerDto.Request request) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if(!customer.getPhone().equals(request.getPhone()) && 
           customerRepository.existsByPhone(request.getPhone())) {
            throw new NumberAlreadyExistException("Number already used");
        }

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());

        return toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID : " + id));

        // Vérifier les abonnements actifs via le repository
        boolean hasActiveSubscriptions = subscriptionRepository.existsByCustomerAndActiveTrue(customer);
        
        if(hasActiveSubscriptions) {
            throw new ActiveSubscriptionExistException("Can not delete customer, active subscription");
        }

        // Journalisation avant suppression
        logger.info("Suppression du client {} {}", customer.getFirstName(), customer.getLastName());
        
        customerRepository.delete(customer);
    }

    private CustomerDto.Response toResponse(Customer customer) {
    // Récupérer l'abonnement actif complet
    Optional<Subscription> activeSubscription = 
        subscriptionRepository.findActiveByCustomerId(customer.getId());

    return CustomerDto.Response.builder()
        .id(customer.getId())
        .fullName(String.format("%s %s", customer.getFirstName(), customer.getLastName()))
        .phone(formatPhoneNumber(customer.getPhone()))
        .registrationDate(customer.getRegistrationDate().format(DateTimeFormatter.ISO_DATE))
        .activeSubscription(
            activeSubscription.map(sub -> 
                SubscriptionDto.Response.builder()
                .id(sub.getId())
                .startDate(sub.getStartDate())
                .active(sub.isActive())
                .pack(PackDto.Response.fromEntity(sub.getPack())) 
                .build()
            ).orElse(null)
        )
        .subscriptionCount(
            customer.getSubscriptions() != null ? 
            customer.getSubscriptions().size() : 
            0
        )
        .build();
}


    // Méthode utilitaire pour le formatage des numéros
    private String formatPhoneNumber(String phone) {
        return phone.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{2})", "+$1 $2 $3 $4");
    }

    // CustomerService.java
    public Customer getById(Long id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
    }

    public CustomerDto.Response getCustomerById(Long id) {
        Customer customer = customerRepository.findByIdWithSubscriptions(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
        return CustomerDto.Response.fromEntity(customer);
    }
    
    
    public Customer getCustomerWithSubscriptions(Long id) {
        return customerRepository.findByIdWithSubscriptions(id)
            .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));
    }
    

}
