package com.itu.aviation.controller;

import com.itu.aviation.entity.MembreEquipe;
import com.itu.aviation.entity.TypeMembre;
import com.itu.aviation.service.MembreEquipeService;
import com.itu.aviation.repository.TypeMembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipages")
public class EquipageController {

    @Autowired
    private MembreEquipeService membreEquipeService;

    @Autowired
    private TypeMembreRepository typeMembreRepository;

    @GetMapping
    public ModelAndView list(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) String qualifications,
            @RequestParam(required = false) Integer heuresVolMin,
            @RequestParam(required = false) Integer heuresVolMax) {
        
        ModelAndView mav = new ModelAndView("equipages/list");
        mav.addObject("pageTitle", "Gestion des Équipages");
        mav.addObject("currentPage", "equipages");
        
        List<MembreEquipe> allMembres = membreEquipeService.findAll();
        
        // Filtre multi-critères
        List<MembreEquipe> filtered = allMembres.stream().filter(m -> {
            // Filtre par nom
            if (nom != null && !nom.isBlank()) {
                if (m.getNom() == null || !m.getNom().toLowerCase().contains(nom.toLowerCase())) {
                    return false;
                }
            }
            // Filtre par prénom
            if (prenom != null && !prenom.isBlank()) {
                if (m.getPrenom() == null || !m.getPrenom().toLowerCase().contains(prenom.toLowerCase())) {
                    return false;
                }
            }
            // Filtre par type de membre
            if (typeId != null) {
                if (m.getTypeMembre() == null || !typeId.equals(m.getTypeMembre().getId())) {
                    return false;
                }
            }
            // Filtre par qualifications
            if (qualifications != null && !qualifications.isBlank()) {
                if (m.getQualifications() == null || !m.getQualifications().toLowerCase().contains(qualifications.toLowerCase())) {
                    return false;
                }
            }
            // Filtre par heures de vol minimum
            if (heuresVolMin != null) {
                if (m.getHeuresVol() == null || m.getHeuresVol() < heuresVolMin) {
                    return false;
                }
            }
            // Filtre par heures de vol maximum
            if (heuresVolMax != null) {
                if (m.getHeuresVol() == null || m.getHeuresVol() > heuresVolMax) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        
        mav.addObject("membres", filtered);
        mav.addObject("types", typeMembreRepository.findAll());
        
        // Conserver les valeurs des filtres
        mav.addObject("filterNom", nom);
        mav.addObject("filterPrenom", prenom);
        mav.addObject("filterTypeId", typeId);
        mav.addObject("filterQualifications", qualifications);
        mav.addObject("filterHeuresVolMin", heuresVolMin);
        mav.addObject("filterHeuresVolMax", heuresVolMax);
        
        return mav;
    }
}
