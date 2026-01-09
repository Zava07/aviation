package com.itu.aviation.service;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.BilletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BilletService {

    private final BilletRepository billetRepository;

    public BilletService(BilletRepository billetRepository) {
        this.billetRepository = billetRepository;
    }

    public List<Billet> findAll() {
        return billetRepository.findAll();
    }

    public Billet findById(Long id) {
        return billetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Billet not found: " + id));
    }

    public Billet findByNumero(String numero) {
        return billetRepository.findByNumeroBillet(numero);
    }

    public Billet create(Billet b) {
        if (b.getPrixFinal() == null && b.getReservation() != null && b.getReservation().getPrix() != null) {
            b.setPrixFinal(b.getReservation().getPrix());
        }
        return billetRepository.save(b);
    }

    public Billet update(Long id, Billet b) {
        if (!billetRepository.existsById(id))
            throw new ResourceNotFoundException("Billet not found: " + id);
        b.setId(id);
        return billetRepository.save(b);
    }

    public void delete(Long id) {
        billetRepository.deleteById(id);
    }
}