package com.itu.aviation.repository;

import com.itu.aviation.entity.StatusVol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusVolRepository extends JpaRepository<StatusVol, Long> {
}
