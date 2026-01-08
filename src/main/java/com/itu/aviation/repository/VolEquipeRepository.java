package com.itu.aviation.repository;

import com.itu.aviation.entity.VolEquipe;
import com.itu.aviation.entity.VolEquipeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolEquipeRepository extends JpaRepository<VolEquipe, VolEquipeId> {
}
