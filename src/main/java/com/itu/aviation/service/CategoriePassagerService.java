package com.itu.aviation.service;

import com.itu.aviation.entity.CategoriePassager;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.CategoriePassagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriePassagerService {

    private final CategoriePassagerRepository categoriePassagerRepository;

    public CategoriePassagerService(CategoriePassagerRepository categoriePassagerRepository) {
        this.categoriePassagerRepository = categoriePassagerRepository;
    }

    public List<CategoriePassager> findAll() {
        return categoriePassagerRepository.findAll();
    }

    public CategoriePassager findById(Long id) {
        return categoriePassagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CategoriePassager not found: " + id));
    }

    public CategoriePassager create(CategoriePassager categorie) {
        return categoriePassagerRepository.save(categorie);
    }

    public CategoriePassager update(Long id, CategoriePassager categorie) {
        if (!categoriePassagerRepository.existsById(id)) {
            throw new ResourceNotFoundException("CategoriePassager not found: " + id);
        }
        categorie.setId(id);
        return categoriePassagerRepository.save(categorie);
    }

    public void delete(Long id) {
        if (!categoriePassagerRepository.existsById(id)) {
            throw new ResourceNotFoundException("CategoriePassager not found: " + id);
        }
        categoriePassagerRepository.deleteById(id);
    }
}
