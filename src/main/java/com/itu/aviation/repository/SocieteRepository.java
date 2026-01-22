package com.itu.aviation.repository;

import com.itu.aviation.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocieteRepository extends JpaRepository<Societe, Long> {
    Optional<Societe> findByNomSocieteIgnoreCase(String nomSociete);
    boolean existsByNomSocieteIgnoreCase(String nomSociete);
}