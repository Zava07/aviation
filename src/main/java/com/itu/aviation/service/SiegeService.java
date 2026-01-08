package com.itu.aviation.service;

import com.itu.aviation.entity.Siege;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.SiegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiegeService {

    private final SiegeRepository siegeRepository;

    public SiegeService(SiegeRepository siegeRepository) {
        this.siegeRepository = siegeRepository;
    }

    public List<Siege> findAll() {
        return siegeRepository.findAll();
    }

    public Siege findById(Long id) {
        return siegeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Siege not found: " + id));
    }

    public Siege findByNumero(String numero) {
        return siegeRepository.findByNumero(numero);
    }

    public Siege create(Siege s) {
        return siegeRepository.save(s);
    }

    public Siege update(Long id, Siege s) {
        if (!siegeRepository.existsById(id))
            throw new ResourceNotFoundException("Siege not found: " + id);
        s.setId(id);
        return siegeRepository.save(s);
    }

    public void delete(Long id) {
        siegeRepository.deleteById(id);
    }
}