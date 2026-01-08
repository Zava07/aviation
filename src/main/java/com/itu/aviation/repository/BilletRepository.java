package com.itu.aviation.repository;

import com.itu.aviation.entity.Billet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BilletRepository extends JpaRepository<Billet, Long> {
    Billet findByNumeroBillet(String numero);
}
