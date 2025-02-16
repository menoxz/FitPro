package com.gymmanager.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gymmanager.model.Customer;
import com.gymmanager.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    @Query("SELECT s FROM Subscription s WHERE s.customer.id = :customerId AND s.active = true ORDER BY s.startDate DESC")
    Optional<Subscription> findActiveByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT s FROM Subscription s WHERE s.customer.id = :customerId AND s.active = true ")
    List<Subscription> findActiveSubByCustomerId(@Param("customerId") Long customerId);
    
    List<Subscription> findByCustomerId(Long customerId);

      @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
           "FROM Subscription s WHERE s.customer = :customer AND s.active = true")
    boolean existsByCustomerAndActiveTrue(@Param("customer") Customer customer);

    @Modifying
    @Query("UPDATE Subscription s SET s.active = false WHERE s.customer.id = :customerId AND s.active = true")
    void deactivateAllActiveSubscriptions(@Param("customerId") Long customerId);

    Optional<Subscription> findActiveByCustomer(Customer customer);

     @Query("SELECT COUNT(DISTINCT s.customer) FROM Subscription s WHERE s.active = true")
    long countActiveCustomers();

    @Query("SELECT SUM(p.monthlyPrice) FROM Subscription s JOIN s.pack p WHERE s.active = true AND MONTH(s.startDate) = :currentMonth AND YEAR(s.startDate) = :currentYear")
    BigDecimal calculateMonthlyRevenue(@Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear);

    @Query("SELECT s FROM Subscription s WHERE s.startDate BETWEEN :startDate AND :endDate")
    List<Subscription> findSubscriptionsInPeriod(LocalDate startDate, LocalDate endDate);

    List<Subscription> findByActiveTrue();

    @Query("SELECT s FROM Subscription s " +
       "JOIN s.customer c " +
       "JOIN s.pack p " +
       "WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) ")
    Page<Subscription> searchPage(String query, Pageable pageable);
    
    @Query("SELECT new map(YEAR(s.startDate) as year, MONTH(s.startDate) as month, SUM(p.monthlyPrice) as revenue) " +
           "FROM Subscription s JOIN s.pack p GROUP BY YEAR(s.startDate), MONTH(s.startDate) ORDER BY year, month")
    List<Map<String, Object>> getRevenueByMonth();

}
