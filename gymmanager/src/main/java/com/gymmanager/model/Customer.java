package com.gymmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers", 
    uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @CreationTimestamp
    @Column(name = "registration_date", updatable = false)
    private LocalDate registrationDate;

    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "Format national requis")
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;


    

    
}
