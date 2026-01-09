package com.itu.aviation.repository;

import com.itu.aviation.entity.Passager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {
	Optional<Passager> findByNomAndPrenomAndEmailIgnoreCase(String nom, String prenom, String email);
}
