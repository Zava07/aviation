package com.itu.aviation.repository;

import com.itu.aviation.entity.CategoriePassager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriePassagerRepository extends JpaRepository<CategoriePassager, Long> {

}
