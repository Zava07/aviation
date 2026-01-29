package com.itu.aviation.service;

import com.itu.aviation.entity.ProduitExtra;
import com.itu.aviation.entity.VenteProduitExtra;
import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.VenteProduitExtraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VenteProduitExtraService {

    private final VenteProduitExtraRepository venteProduitExtraRepository;
    private final ProduitExtraService produitExtraService;
    private final VolInstanceService volInstanceService;

    public VenteProduitExtraService(VenteProduitExtraRepository venteProduitExtraRepository,
                                     ProduitExtraService produitExtraService,
                                     VolInstanceService volInstanceService) {
        this.venteProduitExtraRepository = venteProduitExtraRepository;
        this.produitExtraService = produitExtraService;
        this.volInstanceService = volInstanceService;
    }

    public List<VenteProduitExtra> findAll() {
        return venteProduitExtraRepository.findAllOrderByDateDesc();
    }

    public VenteProduitExtra findById(Long id) {
        return venteProduitExtraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vente non trouvée: " + id));
    }

    public List<VenteProduitExtra> findByProduitId(Long produitId) {
        return venteProduitExtraRepository.findByProduitExtraId(produitId);
    }

    public List<VenteProduitExtra> findByVolInstanceId(Long volInstanceId) {
        return venteProduitExtraRepository.findByVolInstanceId(volInstanceId);
    }

    public List<VenteProduitExtra> findByDate(LocalDate date) {
        return venteProduitExtraRepository.findByDate(date.atStartOfDay());
    }

    public List<VenteProduitExtra> findByPeriode(LocalDateTime start, LocalDateTime end) {
        return venteProduitExtraRepository.findByDateAchatBetween(start, end);
    }

    public BigDecimal getTotalVentes() {
        return venteProduitExtraRepository.getTotalVentes();
    }

    public BigDecimal getTotalVentesByProduit(Long produitId) {
        return venteProduitExtraRepository.getTotalVentesByProduit(produitId);
    }

    public BigDecimal getTotalVentesByVolInstance(Long volInstanceId) {
        return venteProduitExtraRepository.getTotalVentesByVolInstance(volInstanceId);
    }

    public VenteProduitExtra create(VenteProduitExtra vente) {
        if (vente.getProduitExtra() == null || vente.getProduitExtra().getId() == null) {
            throw new IllegalArgumentException("La vente doit être associée à un produit");
        }
        if (vente.getVolInstance() == null || vente.getVolInstance().getId() == null) {
            throw new IllegalArgumentException("La vente doit être associée à une instance de vol");
        }

        ProduitExtra produit = produitExtraService.findById(vente.getProduitExtra().getId());
        VolInstance volInstance = volInstanceService.findById(vente.getVolInstance().getId());

        vente.setProduitExtra(produit);
        vente.setVolInstance(volInstance);

        // Calcul automatique du prix total si non fourni
        if (vente.getPrixTotal() == null && vente.getQuantite() != null && produit.getPrix() != null) {
            vente.setPrixTotal(produit.getPrix().multiply(BigDecimal.valueOf(vente.getQuantite())));
        }

        return venteProduitExtraRepository.save(vente);
    }

    public VenteProduitExtra update(Long id, VenteProduitExtra details) {
        VenteProduitExtra v = findById(id);

        if (details.getQuantite() != null) v.setQuantite(details.getQuantite());
        if (details.getPrixTotal() != null) v.setPrixTotal(details.getPrixTotal());
        if (details.getDateAchat() != null) v.setDateAchat(details.getDateAchat());

        if (details.getProduitExtra() != null && details.getProduitExtra().getId() != null) {
            ProduitExtra p = produitExtraService.findById(details.getProduitExtra().getId());
            v.setProduitExtra(p);
        }
        if (details.getVolInstance() != null && details.getVolInstance().getId() != null) {
            VolInstance vi = volInstanceService.findById(details.getVolInstance().getId());
            v.setVolInstance(vi);
        }

        return venteProduitExtraRepository.save(v);
    }

    public void delete(Long id) {
        if (!venteProduitExtraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vente non trouvée: " + id);
        }
        venteProduitExtraRepository.deleteById(id);
    }
}
