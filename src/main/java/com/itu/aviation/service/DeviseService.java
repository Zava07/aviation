package com.itu.aviation.service;

import com.itu.aviation.entity.Devise;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.DeviseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviseService {
    private final DeviseRepository repo;

    public DeviseService(DeviseRepository repo) {
        this.repo = repo;
    }

    public List<Devise> findAll() {
        return repo.findAll();
    }

    public Devise findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Devise not found: " + id));
    }

    public Devise create(Devise d) {
        return repo.save(d);
    }

    public Devise update(Long id, Devise d) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("Devise not found: " + id);
        d.setId(id);
        return repo.save(d);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}