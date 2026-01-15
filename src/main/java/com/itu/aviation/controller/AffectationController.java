package com.itu.aviation.controller;

import com.itu.aviation.entity.VolEquipe;
import com.itu.aviation.entity.VolEquipeId;
import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.entity.MembreEquipe;
import com.itu.aviation.entity.TypeMembre;
import com.itu.aviation.service.VolEquipeService;
import com.itu.aviation.service.VolInstanceService;
import com.itu.aviation.service.MembreEquipeService;
import com.itu.aviation.repository.TypeMembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/affectations")
public class AffectationController {

    @Autowired
    private VolEquipeService volEquipeService;

    @Autowired
    private VolInstanceService volInstanceService;

    @Autowired
    private MembreEquipeService membreEquipeService;

    @Autowired
    private TypeMembreRepository typeMembreRepository;

    @GetMapping
    public ModelAndView list(
            @RequestParam(required = false) Long volInstanceId,
            @RequestParam(required = false) Long membreId,
            @RequestParam(required = false) Long typeMembreId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String dateDepart) {
        
        ModelAndView mav = new ModelAndView("affectations/list");
        mav.addObject("pageTitle", "Affectation des Équipages");
        mav.addObject("currentPage", "affectations");
        
        List<VolEquipe> allAffectations = volEquipeService.findAll();
        
        // Filtre multi-critères
        List<VolEquipe> filtered = allAffectations.stream().filter(ve -> {
            // Filtre par vol instance
            if (volInstanceId != null) {
                if (ve.getVolInstance() == null || !volInstanceId.equals(ve.getVolInstance().getId())) {
                    return false;
                }
            }
            // Filtre par membre
            if (membreId != null) {
                if (ve.getMembre() == null || !membreId.equals(ve.getMembre().getId())) {
                    return false;
                }
            }
            // Filtre par type de membre
            if (typeMembreId != null) {
                if (ve.getMembre() == null || ve.getMembre().getTypeMembre() == null 
                    || !typeMembreId.equals(ve.getMembre().getTypeMembre().getId())) {
                    return false;
                }
            }
            // Filtre par rôle
            if (role != null && !role.isBlank()) {
                if (ve.getRole() == null || !ve.getRole().toLowerCase().contains(role.toLowerCase())) {
                    return false;
                }
            }
            // Filtre par date de départ
            if (dateDepart != null && !dateDepart.isBlank()) {
                try {
                    LocalDate filterDate = LocalDate.parse(dateDepart);
                    if (ve.getVolInstance() == null || ve.getVolInstance().getDateDepart() == null 
                        || !ve.getVolInstance().getDateDepart().toLocalDate().equals(filterDate)) {
                        return false;
                    }
                } catch (Exception e) {
                    // Ignore parsing errors
                }
            }
            return true;
        }).collect(Collectors.toList());
        
        mav.addObject("affectations", filtered);
        mav.addObject("volInstances", volInstanceService.findAll());
        mav.addObject("membres", membreEquipeService.findAll());
        mav.addObject("typesMembre", typeMembreRepository.findAll());
        
        return mav;
    }
    
    @GetMapping("/affecter")
    public ModelAndView showAffecterForm() {
        ModelAndView mav = new ModelAndView("affectations/form");
        mav.addObject("pageTitle", "Nouvelle Affectation");
        mav.addObject("currentPage", "affectations");
        mav.addObject("volInstances", volInstanceService.findAll());
        mav.addObject("membres", membreEquipeService.findAll());
        mav.addObject("typesMembre", typeMembreRepository.findAll());
        return mav;
    }
    
    @PostMapping("/save")
    public String saveAffectation(
            @RequestParam Long volInstanceId,
            @RequestParam Long membreId,
            @RequestParam(required = false) String role,
            RedirectAttributes redirect) {
        
        try {
            // Récupérer le vol instance
            VolInstance volInstance = volInstanceService.findById(volInstanceId);
            MembreEquipe membre = membreEquipeService.findById(membreId);
            
            // Vérifier si cette affectation existe déjà
            VolEquipeId affectationId = new VolEquipeId(volInstanceId, membreId);
            if (volEquipeService.existsById(affectationId)) {
                redirect.addFlashAttribute("errorMessage", 
                    "Cette personne (" + membre.getNom() + " " + membre.getPrenom() + 
                    ") est déjà affectée à ce vol.");
                return "redirect:/affectations/affecter";
            }
            
            // Vérifier les conflits de dates avec d'autres vols
            List<VolEquipe> conflits = volEquipeService.findConflictingAffectations(
                membreId, 
                volInstance.getDateDepart(), 
                volInstance.getDateArrivee()
            );
            
            if (!conflits.isEmpty()) {
                // Construire le message d'erreur avec les détails des conflits
                StringBuilder errorMsg = new StringBuilder();
                errorMsg.append("Cette personne (")
                        .append(membre.getNom()).append(" ").append(membre.getPrenom())
                        .append(") est déjà affectée à un vol pendant cette période : ");
                
                for (VolEquipe conflit : conflits) {
                    VolInstance viConflit = conflit.getVolInstance();
                    errorMsg.append("\n• Vol #").append(viConflit.getId())
                            .append(" (").append(viConflit.getVol().getAeroportOrigine().getNomAeroport())
                            .append(" → ").append(viConflit.getVol().getAeroportDestination().getNomAeroport())
                            .append(") du ").append(viConflit.getDateDepart().toLocalDate())
                            .append(" ").append(viConflit.getDateDepart().toLocalTime())
                            .append(" au ").append(viConflit.getDateArrivee().toLocalDate())
                            .append(" ").append(viConflit.getDateArrivee().toLocalTime());
                }
                
                redirect.addFlashAttribute("errorMessage", errorMsg.toString());
                return "redirect:/affectations/affecter";
            }
            
            // Créer la nouvelle affectation
            VolEquipe affectation = new VolEquipe();
            affectation.setId(affectationId);
            affectation.setVolInstance(volInstance);
            affectation.setMembre(membre);
            affectation.setRole(role != null && !role.isBlank() ? role : membre.getTypeMembre().getType());
            
            volEquipeService.create(affectation);
            
            redirect.addFlashAttribute("successMessage", 
                "Affectation créée avec succès : " + membre.getNom() + " " + membre.getPrenom() + 
                " affecté(e) au vol #" + volInstanceId);
            
            return "redirect:/affectations";
            
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la création de l'affectation : " + e.getMessage());
            return "redirect:/affectations/affecter";
        }
    }
    
    @PostMapping("/delete/{volInstanceId}/{membreId}")
    public String deleteAffectation(
            @PathVariable Long volInstanceId,
            @PathVariable Long membreId,
            RedirectAttributes redirect) {
        
        try {
            VolEquipeId id = new VolEquipeId(volInstanceId, membreId);
            volEquipeService.delete(id);
            redirect.addFlashAttribute("successMessage", "Affectation supprimée avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        
        return "redirect:/affectations";
    }
}
