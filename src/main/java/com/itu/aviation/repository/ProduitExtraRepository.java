package com.itu.aviation.repository;

import com.itu.aviation.entity.ProduitExtra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitExtraRepository extends JpaRepository<ProduitExtra, Long> {

    List<ProduitExtra> findByNomProduitContainingIgnoreCase(String nom);

    @Query("SELECT p FROM ProduitExtra p WHERE LOWER(p.nomProduit) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<ProduitExtra> search(@Param("q") String q);
}
