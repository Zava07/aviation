package com.itu.aviation.repository;

import com.itu.aviation.entity.TypePassager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePassagerRepository extends JpaRepository<TypePassager, Long> {
}
