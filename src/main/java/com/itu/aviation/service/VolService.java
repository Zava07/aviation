package com.itu.aviation.service;

import com.itu.aviation.entity.Vol;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.VolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolService {

    private final VolRepository volRepository;

    public VolService(VolRepository volRepository) {
        this.volRepository = volRepository;
    }

    public List<Vol> findAll() {
        return volRepository.findAll();
    }

    public Vol findById(Long id) {
        return volRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vol not found: " + id));
    }

    public Vol findByNumero(String numero) {
        return volRepository.findByNumeroVol(numero);
    }

    public Vol create(Vol vol) {
        // business rule: an airplane cannot be assigned to overlapping flights
        if (vol.getAvion() != null) {
            Long avionId = vol.getAvion().getId();
            if (avionId != null) {
                List<Vol> overlaps = volRepository.findOverlapping(avionId, vol.getDateDepart(), vol.getDateArrivee());
                if (overlaps != null && !overlaps.isEmpty()) {
                    throw new IllegalStateException("L'avion est déjà affecté à un vol pendant cette période.");
                }
            }
        }
        return volRepository.save(vol);
    }

    public Vol update(Long id, Vol vol) {
        if (!volRepository.existsById(id))
            throw new ResourceNotFoundException("Vol not found: " + id);
        vol.setId(id);
        if (vol.getAvion() != null) {
            Long avionId = vol.getAvion().getId();
            if (avionId != null) {
                List<Vol> overlaps = volRepository.findOverlapping(avionId, vol.getDateDepart(), vol.getDateArrivee());
                // remove self (existing record) from overlaps
                overlaps.removeIf(v -> v.getId().equals(id));
                if (overlaps != null && !overlaps.isEmpty()) {
                    throw new IllegalStateException("L'avion est déjà affecté à un vol pendant cette période.");
                }
            }
        }
        return volRepository.save(vol);
    }

    public void delete(Long id) {
        volRepository.deleteById(id);
    }
}