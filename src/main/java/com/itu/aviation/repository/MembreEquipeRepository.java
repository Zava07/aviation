package com.itu.aviation.repository;

import com.itu.aviation.entity.MembreEquipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembreEquipeRepository extends JpaRepository<MembreEquipe, Long> {
}
