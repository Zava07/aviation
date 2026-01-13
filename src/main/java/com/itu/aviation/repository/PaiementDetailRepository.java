package com.itu.aviation.repository;

import com.itu.aviation.entity.PaiementDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementDetailRepository extends JpaRepository<PaiementDetail, Long> {
}