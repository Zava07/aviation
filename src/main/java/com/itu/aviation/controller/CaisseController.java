package com.itu.aviation.controller;

import com.itu.aviation.entity.Caisse;
import com.itu.aviation.service.CaisseService;
import com.itu.aviation.service.MouvementCaisseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CaisseController {

    @Autowired
    private CaisseService caisseService;

    @Autowired
    private MouvementCaisseService mouvementCaisseService;

    @GetMapping("/caisses")
    
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("caisses/list");
        mav.addObject("pageTitle", "Caisses");
        mav.addObject("currentPage", "caisses");
        List<Caisse> caisses = caisseService.findAll();
        Map<Long, java.math.BigDecimal> totalCredits = new HashMap<>();
        Map<Long, java.math.BigDecimal> balances = new HashMap<>();
        for (Caisse c : caisses) {
            totalCredits.put(c.getId(), mouvementCaisseService.getTotalCreditsForCaisse(c.getId()));
            balances.put(c.getId(), mouvementCaisseService.getBalanceForCaisse(c.getId()));
        }
        mav.addObject("caisses", caisses);
        mav.addObject("totalCredits", totalCredits);
        mav.addObject("balances", balances);
        // dashboard total chiffre d'affaire (sum of credits across all caisses)
        java.math.BigDecimal totalCA = totalCredits.values().stream().filter(java.util.Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        mav.addObject("totalCA", totalCA);
        return mav;
    }

    @GetMapping("/caisses/{id}")
    public ModelAndView details(@PathVariable Long id,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateFrom,
                                @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateTo,
                                @RequestParam(required = false) String type) {
        ModelAndView mav = new ModelAndView("caisses/detail");
        mav.addObject("pageTitle", "Détails Caisse");
        mav.addObject("currentPage", "caisses");
        Caisse c = caisseService.findById(id);
        mav.addObject("caisse", c);
        // fetch mouvements and apply filters
        List<com.itu.aviation.entity.MouvementCaisse> mv = mouvementCaisseService.findByCaisseId(id);
        java.util.List<java.util.Map<String,Object>> rows = mv.stream().filter(m -> {
            if (dateFrom != null && (m.getDateMouvement() == null || m.getDateMouvement().toLocalDate().isBefore(dateFrom))) return false;
            if (dateTo != null && (m.getDateMouvement() == null || m.getDateMouvement().toLocalDate().isAfter(dateTo))) return false;
            if (type != null && !type.trim().isEmpty()) {
                String t = m.getTypeMouvementCaisse() != null ? m.getTypeMouvementCaisse().getType() : "";
                if (!t.equalsIgnoreCase(type)) return false;
            }
            return true;
        }).map(m -> {
            java.util.Map<String,Object> r = new HashMap<>();
            r.put("id", m.getId());
            r.put("montant", m.getMontant());
            r.put("type", m.getTypeMouvementCaisse() != null ? m.getTypeMouvementCaisse().getType() : null);
            r.put("date", m.getDateMouvement());
            if (m.getPaiement() != null) {
                r.put("paiementId", m.getPaiement().getId());
                if (m.getPaiement().getBillet() != null) {
                    r.put("billetNumero", m.getPaiement().getBillet().getNumeroBillet());
                }
                if (m.getPaiement().getReservation() != null && m.getPaiement().getReservation().getPassager() != null) {
                    r.put("passager", m.getPaiement().getReservation().getPassager().getNom() + " " + m.getPaiement().getReservation().getPassager().getPrenom());
                }
            }
            return r;
        }).collect(Collectors.toList());
        mav.addObject("mouvements", rows);
        mav.addObject("dateFrom", dateFrom);
        mav.addObject("dateTo", dateTo);
        mav.addObject("typeFilter", type);
        mav.addObject("totalCredits", mouvementCaisseService.getTotalCreditsForCaisse(id));
        mav.addObject("balance", mouvementCaisseService.getBalanceForCaisse(id));
        return mav;
    }

    @GetMapping("/mouvement-caisses")
    public ModelAndView mouvements(@RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateFrom,
                                    @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateTo,
                                    @RequestParam(required = false) String type) {
        ModelAndView mav = new ModelAndView("caisses/mouvements");
        mav.addObject("pageTitle", "Mouvements Caisses");
        mav.addObject("currentPage", "mouvement-caisses");
        List<com.itu.aviation.entity.MouvementCaisse> mv = mouvementCaisseService.findAll();
        java.util.List<java.util.Map<String,Object>> rows = mv.stream().filter(m -> {
            if (dateFrom != null && (m.getDateMouvement() == null || m.getDateMouvement().toLocalDate().isBefore(dateFrom))) return false;
            if (dateTo != null && (m.getDateMouvement() == null || m.getDateMouvement().toLocalDate().isAfter(dateTo))) return false;
            if (type != null && !type.trim().isEmpty()) {
                String t = m.getTypeMouvementCaisse() != null ? m.getTypeMouvementCaisse().getType() : "";
                if (!t.equalsIgnoreCase(type)) return false;
            }
            return true;
        }).map(m -> {
            java.util.Map<String,Object> r = new java.util.HashMap<>();
            r.put("id", m.getId());
            r.put("montant", m.getMontant());
            r.put("type", m.getTypeMouvementCaisse() != null ? m.getTypeMouvementCaisse().getType() : null);
            r.put("date", m.getDateMouvement());
            if (m.getCaisse() != null) {
                r.put("caisseId", m.getCaisse().getId());
                r.put("caisseNom", m.getCaisse().getNomCaisse());
            }
            if (m.getPaiement() != null) {
                r.put("paiementId", m.getPaiement().getId());
                if (m.getPaiement().getBillet() != null) {
                    r.put("billetNumero", m.getPaiement().getBillet().getNumeroBillet());
                }
                if (m.getPaiement().getReservation() != null && m.getPaiement().getReservation().getPassager() != null) {
                    r.put("passager", m.getPaiement().getReservation().getPassager().getNom() + " " + m.getPaiement().getReservation().getPassager().getPrenom());
                }
            }
            return r;
        }).collect(java.util.stream.Collectors.toList());
        mav.addObject("mouvements", rows);
        mav.addObject("dateFrom", dateFrom);
        mav.addObject("dateTo", dateTo);
        mav.addObject("typeFilter", type);
        return mav;
    }
}