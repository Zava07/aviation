package com.itu.aviation.repository;

import com.itu.aviation.entity.StatusBillet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusBilletRepository extends JpaRepository<StatusBillet, Long> {
}
