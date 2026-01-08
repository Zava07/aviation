package com.itu.aviation.repository;

import com.itu.aviation.entity.TypeMembre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMembreRepository extends JpaRepository<TypeMembre, Long> {
}
