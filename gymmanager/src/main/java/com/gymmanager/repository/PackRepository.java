package com.gymmanager.repository;

import com.gymmanager.model.Pack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackRepository extends JpaRepository<Pack, Long> {
}
