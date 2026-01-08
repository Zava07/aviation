package com.itu.aviation.service;

import com.itu.aviation.entity.Avion;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.AvionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvionService {

    private final AvionRepository avionRepository;

    public AvionService(AvionRepository avionRepository) {
        this.avionRepository = avionRepository;
    }

    public List<Avion> findAll() {
        return avionRepository.findAll();
    }

    public Avion findById(Long id) {
        return avionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Avion not found: " + id));
    }

    public Avion create(Avion avion) {
        return avionRepository.save(avion);
    }

    public Avion update(Long id, Avion avion) {
        if (!avionRepository.existsById(id))
            throw new ResourceNotFoundException("Avion not found: " + id);
        avion.setId(id);
        return avionRepository.save(avion);
    }

    public void delete(Long id) {
        avionRepository.deleteById(id);
    }
}
