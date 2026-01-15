package com.itu.aviation.controller;

import com.itu.aviation.entity.Passager;
import com.itu.aviation.service.PassagerService;
import com.itu.aviation.service.FidelitePassagerService;
import com.itu.aviation.repository.TypePassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/passagers")
public class PassagerController {

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private TypePassagerRepository typePassagerRepository;

    @Autowired
    private FidelitePassagerService fidelitePassagerService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String nom,
                             @RequestParam(required = false) String prenom,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) Long typeId,
                             @RequestParam(required = false) Integer mois,
                             @RequestParam(required = false) Integer annee,
                             @RequestParam(required = false) Integer pointsMin,
                             @RequestParam(required = false) Integer pointsMax) {
        ModelAndView mav = new ModelAndView("passagers/list");
        mav.addObject("pageTitle", "Passagers");
        mav.addObject("currentPage", "passagers");
        
        // Valeurs par défaut pour mois et année (date actuelle)
        LocalDate now = LocalDate.now();
        Integer selectedMois = mois != null ? mois : now.getMonthValue();
        Integer selectedAnnee = annee != null ? annee : now.getYear();
        
        List<Passager> all = passagerService.findAll();
        
        // Calculer les points par mois/année pour chaque passager
        Map<Long, Integer> pointsParMois = new HashMap<>();
        Map<Long, Integer> pointsParAnnee = new HashMap<>();
        
        for (Passager p : all) {
            Integer pointsMois = fidelitePassagerService.getPointsByMonth(p.getId(), selectedAnnee, selectedMois);
            Integer pointsAn = fidelitePassagerService.getPointsByYear(p.getId(), selectedAnnee);
            pointsParMois.put(p.getId(), pointsMois != null ? pointsMois : 0);
            pointsParAnnee.put(p.getId(), pointsAn != null ? pointsAn : 0);
        }
        
        // Filtrage multi-critères
        List<Passager> filtered = all.stream().filter(p -> {
            if (nom != null && !nom.isBlank()) {
                if (p.getNom() == null || !p.getNom().toLowerCase().contains(nom.toLowerCase())) return false;
            }
            if (prenom != null && !prenom.isBlank()) {
                if (p.getPrenom() == null || !p.getPrenom().toLowerCase().contains(prenom.toLowerCase())) return false;
            }
            if (email != null && !email.isBlank()) {
                if (p.getEmail() == null || !p.getEmail().toLowerCase().contains(email.toLowerCase())) return false;
            }
            if (typeId != null) {
                if (p.getTypePassager() == null || !typeId.equals(p.getTypePassager().getId())) return false;
            }
            // Filtre par points minimum du mois sélectionné
            if (pointsMin != null) {
                Integer pts = pointsParMois.get(p.getId());
                if (pts == null || pts < pointsMin) return false;
            }
            // Filtre par points maximum du mois sélectionné
            if (pointsMax != null) {
                Integer pts = pointsParMois.get(p.getId());
                if (pts == null || pts > pointsMax) return false;
            }
            return true;
        }).collect(Collectors.toList());
        
        mav.addObject("passagers", filtered);
        mav.addObject("types", typePassagerRepository.findAll());
        mav.addObject("pointsParMois", pointsParMois);
        mav.addObject("pointsParAnnee", pointsParAnnee);
        mav.addObject("selectedMois", selectedMois);
        mav.addObject("selectedAnnee", selectedAnnee);
        
        // Liste des mois pour le filtre
        List<Map<String, Object>> listeMois = new ArrayList<>();
        String[] nomsMois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", 
                             "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        for (int i = 1; i <= 12; i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("value", i);
            m.put("label", nomsMois[i-1]);
            listeMois.add(m);
        }
        mav.addObject("listeMois", listeMois);
        
        // Liste des années (5 dernières années)
        List<Integer> listeAnnees = new ArrayList<>();
        int currentYear = now.getYear();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            listeAnnees.add(i);
        }
        mav.addObject("listeAnnees", listeAnnees);
        
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("passagers/list");
        mav.addObject("pageTitle", "Passagers");
        mav.addObject("currentPage", "passagers");
        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("types", typePassagerRepository.findAll());
        mav.addObject("editPassager", passagerService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Passager passager, RedirectAttributes redirect) {
        if (passager.getId() == null) {
            passagerService.create(passager);
            redirect.addFlashAttribute("successMessage", "Passager créé");
        } else {
            passagerService.update(passager.getId(), passager);
            redirect.addFlashAttribute("successMessage", "Passager mis à jour");
        }
        return "redirect:/passagers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        passagerService.delete(id);
        redirect.addFlashAttribute("successMessage", "Passager supprimé");
        return "redirect:/passagers";
    }
}
