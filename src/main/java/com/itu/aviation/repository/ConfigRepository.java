package com.itu.aviation.repository;

import com.itu.aviation.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
    // Récupérer la configuration la plus récente
    Optional<Config> findTopByOrderByDateModificationDesc();
}
