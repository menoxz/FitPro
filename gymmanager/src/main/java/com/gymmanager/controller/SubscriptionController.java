package com.gymmanager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gymmanager.dto.CustomerDto;
import com.gymmanager.dto.SubscriptionDto;
import com.gymmanager.services.SubscriptionExportService;
import com.gymmanager.services.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final SubscriptionExportService subscriptionExportService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto.Response create(@Valid @RequestBody SubscriptionDto.Create request) {
        return subscriptionService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        subscriptionService.cancel(id);
    }

    @GetMapping("/search")
    public Page<SubscriptionDto.Response> search(
        @RequestParam String query, 
        Pageable pageable
    ) {
        return subscriptionService.search(query, pageable);
    }


    @GetMapping("/customer/{customerId}")
    public List<SubscriptionDto.Response> getHistory(@PathVariable Long customerId) {
        return subscriptionService.getCustomerSubscriptions(customerId);
    }

    @GetMapping
    public List<SubscriptionDto.Response> getAll() {
        return subscriptionService.getAll();
    }
    @GetMapping("/pages")
    public List<SubscriptionDto.Response> getAllPage() {
        return subscriptionService.getAllPage();
    }

    @Operation(summary = "Vérifier l'état d'un abonnement")
    @GetMapping("/{id}/status")
    public ResponseEntity<Boolean> checkSubscriptionStatus(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.isSubscriptionActive(id));
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportSubscriptions() {
        byte[] csvData = subscriptionExportService.exportSubscriptionsToCsv();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header("Content-Disposition", "attachment; filename=subscriptions.csv")
                .body(csvData);
    }

}
