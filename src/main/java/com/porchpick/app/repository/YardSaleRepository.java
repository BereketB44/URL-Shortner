package com.porchpick.app.repository;

import com.porchpick.app.model.YardSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface YardSaleRepository extends JpaRepository<YardSale, UUID> {
    List<YardSale> findAllByUserId(UUID userId);
} 