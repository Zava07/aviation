package com.itu.aviation.repository;

import com.itu.aviation.entity.SiegeVol;
import com.itu.aviation.entity.SiegeVolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiegeVolRepository extends JpaRepository<SiegeVol, SiegeVolId> {
    java.util.Optional<SiegeVol> findByVol_IdAndSiege_Id(Long volId, Long siegeId);
    java.util.List<SiegeVol> findByVol_Id(Long volId);
}
