package com.itu.aviation.repository;

import com.itu.aviation.entity.StatusSiegeVol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusSiegeVolRepository extends JpaRepository<StatusSiegeVol, Long> {
}
