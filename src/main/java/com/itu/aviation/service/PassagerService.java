package com.itu.aviation.service;

import com.itu.aviation.entity.Passager;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PassagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassagerService {

    private final PassagerRepository passagerRepository;

    public PassagerService(PassagerRepository passagerRepository) {
        this.passagerRepository = passagerRepository;
    }

    public List<Passager> findAll() {
        return passagerRepository.findAll();
    }

    public Passager findById(Long id) {
        return passagerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passager not found: " + id));
    }

    public java.util.Optional<Passager> findByNomPrenomEmail(String nom, String prenom, String email) {
        return passagerRepository.findByNomAndPrenomAndEmailIgnoreCase(nom, prenom, email);
    }

    public Passager create(Passager p) {
        return passagerRepository.save(p);
    }

    public Passager update(Long id, Passager p) {
        if (!passagerRepository.existsById(id))
            throw new ResourceNotFoundException("Passager not found: " + id);
        p.setId(id);
        return passagerRepository.save(p);
    }

    public void delete(Long id) {
        passagerRepository.deleteById(id);
    }
}