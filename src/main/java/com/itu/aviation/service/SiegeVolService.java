package com.itu.aviation.service;

import com.itu.aviation.entity.SiegeVol;
import com.itu.aviation.entity.SiegeVolId;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.SiegeVolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiegeVolService {
    private final SiegeVolRepository repo;

    public SiegeVolService(SiegeVolRepository repo) {
        this.repo = repo;
    }

    public List<SiegeVol> findAll() {
        return repo.findAll();
    }

    public SiegeVol findById(SiegeVolId id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SiegeVol not found: " + id));
    }

    public SiegeVol create(SiegeVol s) {
        return repo.save(s);
    }

    public SiegeVol update(SiegeVolId id, SiegeVol s) {
        if (!repo.existsById(id))
            throw new ResourceNotFoundException("SiegeVol not found: " + id);
        s.setId(id);
        return repo.save(s);
    }

    public void delete(SiegeVolId id) {
        repo.deleteById(id);
    }

    public java.util.List<SiegeVol> findByVolInstance_Id(Long volInstanceId) {
        return repo.findByVolInstance_Id(volInstanceId);
    }
}