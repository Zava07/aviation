package com.itu.aviation.service;

import com.itu.aviation.entity.TypeBagage;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.TypeBagageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeBagageService {
    private final TypeBagageRepository repo;

    public TypeBagageService(TypeBagageRepository repo) {
        this.repo = repo;
    }

    public List<TypeBagage> findAll() {
        return repo.findAll();
    }

    public TypeBagage findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("TypeBagage not found: " + id));
    }

    public TypeBagage create(TypeBagage t) {
        return repo.save(t);
    }

    public TypeBagage update(Long id, TypeBagage t) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("TypeBagage not found: " + id);
        t.setId(id);
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}