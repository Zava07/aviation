package com.itu.aviation.controller;

import com.itu.aviation.entity.Aeroport;
import com.itu.aviation.entity.MembreEquipe;
import com.itu.aviation.service.MembreEquipeService;
import com.itu.aviation.repository.TypeMembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/personnel")
public class PersonnelController {

    @Autowired
    private MembreEquipeService membreEquipeService;

    @Autowired
    private TypeMembreRepository typeMembreRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String nom,
                             @RequestParam(required = false) String prenom,
                             @RequestParam(required = false) Long typeId) {
        ModelAndView mav = new ModelAndView("personnel/list");
        mav.addObject("pageTitle", "Personnel");
        mav.addObject("currentPage", "personnel");
        java.util.List<com.itu.aviation.entity.MembreEquipe> all = membreEquipeService.findAll();
        java.util.List<com.itu.aviation.entity.MembreEquipe> filtered = all.stream().filter(m -> {
            if (nom != null && !nom.isBlank()) {
                if (m.getNom() == null || !m.getNom().toLowerCase().contains(nom.toLowerCase())) return false;
            }
            if (prenom != null && !prenom.isBlank()) {
                if (m.getPrenom() == null || !m.getPrenom().toLowerCase().contains(prenom.toLowerCase())) return false;
            }
            if (typeId != null) {
                if (m.getTypeMembre() == null || !typeId.equals(m.getTypeMembre().getId())) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("membres", filtered);
        mav.addObject("types", typeMembreRepository.findAll());
        return mav;
    }

    // route alternative used by layout
    @GetMapping("/equipages")
    public String equipages() {
        return "redirect:/personnel";
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("personnel/form");
        mav.addObject("pageTitle", "Nouveau Personnel");
        mav.addObject("currentPage", "personnel");
        mav.addObject("typeMembre", typeMembreRepository.findAll());
        mav.addObject("editPersonnel", new MembreEquipe());
        return mav;
    }
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("personnel/list");
        mav.addObject("pageTitle", "Personnel");
        mav.addObject("currentPage", "personnel");
        mav.addObject("membres", membreEquipeService.findAll());
        mav.addObject("types", typeMembreRepository.findAll());
        mav.addObject("editMembre", membreEquipeService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MembreEquipe membre, RedirectAttributes redirect) {
        if (membre.getId() == null) {
            membreEquipeService.create(membre);
            redirect.addFlashAttribute("successMessage", "Membre créé");
        } else {
            membreEquipeService.update(membre.getId(), membre);
            redirect.addFlashAttribute("successMessage", "Membre mis à jour");
        }
        return "redirect:/personnel";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        membreEquipeService.delete(id);
        redirect.addFlashAttribute("successMessage", "Membre supprimé");
        return "redirect:/personnel";
    }
}
