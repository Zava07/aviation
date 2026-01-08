package com.itu.aviation.repository;

import com.itu.aviation.entity.ModeleAvion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeleAvionRepository extends JpaRepository<ModeleAvion, Long> {
}
