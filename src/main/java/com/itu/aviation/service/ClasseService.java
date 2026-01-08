package com.itu.aviation.service;

import com.itu.aviation.entity.Classe;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ClasseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClasseService {
    private final ClasseRepository repo;

    public ClasseService(ClasseRepository repo) {
        this.repo = repo;
    }

    public List<Classe> findAll() {
        return repo.findAll();
    }

    public Classe findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Classe not found: " + id));
    }

    public Classe create(Classe c) {
        return repo.save(c);
    }

    public Classe update(Long id, Classe c) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("Classe not found: " + id);
        c.setId(id);
        return repo.save(c);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}