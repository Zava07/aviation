package com.itu.aviation.repository;

import com.itu.aviation.entity.Bagage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagageRepository extends JpaRepository<Bagage, Long> {
}
