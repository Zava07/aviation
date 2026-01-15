package com.itu.aviation.service;

import com.itu.aviation.entity.Config;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<Config> findAll() {
        return configRepository.findAll();
    }

    public Config findById(Long id) {
        return configRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found: " + id));
    }

    /**
     * Récupère la configuration active (la plus récente)
     */
    public Optional<Config> findCurrentConfig() {
        return configRepository.findTopByOrderByDateModificationDesc();
    }

    /**
     * Récupère les points de fidélité configurés
     */
    public Integer getPointsFidelite() {
        return findCurrentConfig()
                .map(Config::getPointFidelite)
                .orElse(0);
    }

    public Config create(Config config) {
        return configRepository.save(config);
    }

    public Config update(Long id, Config config) {
        if (!configRepository.existsById(id)) {
            throw new ResourceNotFoundException("Configuration not found: " + id);
        }
        config.setId(id);
        return configRepository.save(config);
    }

    public void delete(Long id) {
        if (!configRepository.existsById(id)) {
            throw new ResourceNotFoundException("Configuration not found: " + id);
        }
        configRepository.deleteById(id);
    }
}
