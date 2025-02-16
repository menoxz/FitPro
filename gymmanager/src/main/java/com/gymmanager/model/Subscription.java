package com.gymmanager.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "pack_id", nullable = false)
    private Pack pack;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    private BigDecimal monthlyPrice;

    public LocalDate calculateEndDate() {
        return startDate.plusMonths(pack.getDurationInMonths());
    }

    public boolean isActive() {
        return active && LocalDate.now().isBefore(calculateEndDate());
    }

}
