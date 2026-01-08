package com.itu.aviation.repository;

import com.itu.aviation.entity.Siege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiegeRepository extends JpaRepository<Siege, Long> {
    Siege findByNumero(String numero);
}
