package com.itu.aviation.controller;

import com.itu.aviation.entity.Caisse;
import com.itu.aviation.service.MouvementCaisseService;
import com.itu.aviation.service.CaisseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final CaisseService caisseService;

    private final MouvementCaisseService mouvementCaisseService;

    public ApiController(CaisseService caisseService, MouvementCaisseService mouvementCaisseService) {
        this.caisseService = caisseService;
        this.mouvementCaisseService = mouvementCaisseService;
    }

    @GetMapping("/api/caisses/{caisseId}/details")
    public Map<String,Object> getCaisseDetails(@PathVariable Long caisseId) {
        Map<String,Object> res = new HashMap<>();
        Caisse c = caisseService.findById(caisseId);
        res.put("id", c.getId());
        res.put("nom", c.getNomCaisse());
        res.put("solde", c.getSolde());
        res.put("balanceComputed", mouvementCaisseService.getBalanceForCaisse(caisseId));
        res.put("totalCredits", mouvementCaisseService.getTotalCreditsForCaisse(caisseId));
        List<com.itu.aviation.entity.MouvementCaisse> mv = mouvementCaisseService.findByCaisseId(caisseId);
        List<Map<String,Object>> mvs = mv.stream().map(m -> Map.<String,Object>of(
            "id", m.getId(),
            "montant", m.getMontant(),
            "type", m.getTypeMouvementCaisse() != null ? m.getTypeMouvementCaisse().getType() : null,
            "date", m.getDateMouvement(),
            "paiementId", m.getPaiement() != null ? m.getPaiement().getId() : null
        )).collect(Collectors.toList());
        res.put("mouvements", mvs);
        return res;
    }

    @GetMapping("/api/caisses/by-methode/{methodeId}")
    public List<Map<String, Object>> getCaissesByMethode(@PathVariable Long methodeId) {
        if (methodeId == null) return java.util.Collections.emptyList();
        List<Caisse> cs = caisseService.findByMethodeId(methodeId);
        return cs.stream().map(c -> Map.<String,Object>of("id", c.getId(), "nom", c.getNomCaisse())).collect(Collectors.toList());
    }


}