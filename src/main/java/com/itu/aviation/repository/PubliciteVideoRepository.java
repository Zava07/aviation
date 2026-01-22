package com.itu.aviation.repository;

import com.itu.aviation.entity.PubliciteVideo;
import com.itu.aviation.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PubliciteVideoRepository extends JpaRepository<PubliciteVideo, Long> {

    List<PubliciteVideo> findBySociete(Societe societe);
}
