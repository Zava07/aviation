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

    public java.util.List<Avion> findAvailable(java.time.LocalDateTime dateDepart, java.time.LocalDateTime dateArrivee, com.itu.aviation.repository.VolInstanceRepository volInstanceRepository) {
        java.util.List<Avion> all = avionRepository.findAll();
        if (dateDepart == null || dateArrivee == null) return all;
        java.util.List<com.itu.aviation.entity.VolInstance> allInstances = volInstanceRepository.findAll();
        java.util.Set<Long> busy = new java.util.HashSet<>();
        for (com.itu.aviation.entity.VolInstance vi : allInstances) {
            if (vi.getAvion() == null || vi.getDateDepart() == null || vi.getDateArrivee() == null) continue;
            if (vi.getDateDepart().isBefore(dateArrivee) && vi.getDateArrivee().isAfter(dateDepart)) {
                if (vi.getAvion().getId() != null) busy.add(vi.getAvion().getId());
            }
        }
        java.util.List<Avion> res = new java.util.ArrayList<>();
        for (Avion a : all) if (!busy.contains(a.getId())) res.add(a);
        return res;
    }
}
