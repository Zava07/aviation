package com.itu.aviation.service;

import com.itu.aviation.entity.PubliciteVideo;
import com.itu.aviation.entity.Societe;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.PubliciteVideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PubliciteVideoService {

    private final PubliciteVideoRepository publiciteVideoRepository;
    private final SocieteService societeService;

    public PubliciteVideoService(PubliciteVideoRepository publiciteVideoRepository, SocieteService societeService) {
        this.publiciteVideoRepository = publiciteVideoRepository;
        this.societeService = societeService;
    }

    public List<PubliciteVideo> findAll() {
        return publiciteVideoRepository.findAll();
    }

    public PubliciteVideo findById(Long id) {
        return publiciteVideoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publicité vidéo non trouvée: " + id));
    }

    public PubliciteVideo save(PubliciteVideo publiciteVideo) {
        if (publiciteVideo.getSociete() == null || publiciteVideo.getSociete().getId() == null) {
            throw new IllegalArgumentException("La vidéo doit être associée à une société");
        }
        Societe societe = societeService.findById(publiciteVideo.getSociete().getId());

        if (publiciteVideo.getId() != null) {
            PubliciteVideo existing = findById(publiciteVideo.getId());
            existing.setTitre(publiciteVideo.getTitre());
            existing.setDureeSeconde(publiciteVideo.getDureeSeconde());
            existing.setSociete(societe);
            return publiciteVideoRepository.save(existing);
        }

        publiciteVideo.setSociete(societe);
        return publiciteVideoRepository.save(publiciteVideo);
    }

    public void delete(Long id) {
        if (!publiciteVideoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publicité vidéo non trouvée: " + id);
        }
        publiciteVideoRepository.deleteById(id);
    }
}
