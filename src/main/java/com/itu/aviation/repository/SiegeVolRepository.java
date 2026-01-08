package com.itu.aviation.repository;

import com.itu.aviation.entity.SiegeVol;
import com.itu.aviation.entity.SiegeVolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiegeVolRepository extends JpaRepository<SiegeVol, SiegeVolId> {
}
