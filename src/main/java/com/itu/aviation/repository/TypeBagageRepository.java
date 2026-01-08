package com.itu.aviation.repository;

import com.itu.aviation.entity.TypeBagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeBagageRepository extends JpaRepository<TypeBagage, Long> {
}
