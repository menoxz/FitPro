package com.gymmanager.repository;

import com.gymmanager.model.Customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Customer> searchByName(String query, Pageable pageable);

    boolean existsByPhone(String phone);
    
     @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.subscriptions WHERE c.id = :id")
    Optional<Customer> findByIdWithSubscriptions(@Param("id") Long id);

    
}

