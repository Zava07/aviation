package com.itu.aviation.service;

import com.itu.aviation.entity.TypeMouvementCaisse;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.TypeMouvementCaisseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeMouvementCaisseService {

    private final TypeMouvementCaisseRepository repo;

    public TypeMouvementCaisseService(TypeMouvementCaisseRepository repo) {
        this.repo = repo;
    }

    public List<TypeMouvementCaisse> findAll() {
        return repo.findAll();
    }

    public TypeMouvementCaisse findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypeMouvementCaisse not found: " + id));
    }

    public TypeMouvementCaisse create(TypeMouvementCaisse t) {
        return repo.save(t);
    }

    public TypeMouvementCaisse update(Long id, TypeMouvementCaisse t) {
        TypeMouvementCaisse existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypeMouvementCaisse not found: " + id));
        existing.setType(t.getType());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}