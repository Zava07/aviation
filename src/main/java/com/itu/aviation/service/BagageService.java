package com.itu.aviation.service;

import com.itu.aviation.entity.Bagage;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.BagageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BagageService {
    private final BagageRepository repo;

    public BagageService(BagageRepository repo) {
        this.repo = repo;
    }

    public List<Bagage> findAll() {
        return repo.findAll();
    }

    public Bagage findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bagage not found: " + id));
    }

    public Bagage create(Bagage b) {
        return repo.save(b);
    }

    public Bagage update(Long id, Bagage b) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("Bagage not found: " + id);
        b.setId(id);
        return repo.save(b);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}