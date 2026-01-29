package com.itu.aviation.service;

import com.itu.aviation.entity.ProduitExtra;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ProduitExtraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProduitExtraService {

    private final ProduitExtraRepository produitExtraRepository;

    public ProduitExtraService(ProduitExtraRepository produitExtraRepository) {
        this.produitExtraRepository = produitExtraRepository;
    }

    public List<ProduitExtra> findAll() {
        return produitExtraRepository.findAll();
    }

    public ProduitExtra findById(Long id) {
        return produitExtraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit Extra non trouvé: " + id));
    }

    public List<ProduitExtra> search(String q) {
        if (q == null || q.isBlank()) {
            return findAll();
        }
        return produitExtraRepository.search(q);
    }

    public ProduitExtra create(ProduitExtra produit) {
        return produitExtraRepository.save(produit);
    }

    public ProduitExtra update(Long id, ProduitExtra details) {
        ProduitExtra p = findById(id);
        if (details.getNomProduit() != null) p.setNomProduit(details.getNomProduit());
        if (details.getDescription() != null) p.setDescription(details.getDescription());
        if (details.getPrix() != null) p.setPrix(details.getPrix());
        return produitExtraRepository.save(p);
    }

    public void delete(Long id) {
        if (!produitExtraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit Extra non trouvé: " + id);
        }
        produitExtraRepository.deleteById(id);
    }
}
