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
        // Vol (route) creation: scheduling is handled via VolInstance
        // Initialize pointVol to 0 for new vols
        if (vol.getPointVol() == null) {
            vol.setPointVol(0);
        }
        return volRepository.save(vol);
    }

    public Vol update(Long id, Vol vol) {
        if (!volRepository.existsById(id))
            throw new ResourceNotFoundException("Vol not found: " + id);
        vol.setId(id);
        // scheduling and overlap checks belong to VolInstance
        return volRepository.save(vol);
    }

    public void delete(Long id) {
        volRepository.deleteById(id);
    }
}