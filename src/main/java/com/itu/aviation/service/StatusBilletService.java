package com.itu.aviation.service;

import com.itu.aviation.entity.StatusBillet;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.StatusBilletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusBilletService {

    private final StatusBilletRepository repo;

    public StatusBilletService(StatusBilletRepository repo) {
        this.repo = repo;
    }

    public List<StatusBillet> findAll() {
        return repo.findAll();
    }

    public StatusBillet findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("StatusBillet not found: " + id));
    }

    public StatusBillet create(StatusBillet s) {
        return repo.save(s);
    }

    public StatusBillet update(Long id, StatusBillet s) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("StatusBillet not found: " + id);
        s.setId(id);
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
