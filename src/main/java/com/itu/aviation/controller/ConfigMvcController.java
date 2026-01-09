package com.itu.aviation.controller;

import com.itu.aviation.entity.Aeroport;
import com.itu.aviation.entity.Classe;
import com.itu.aviation.entity.ModeleAvion;
import com.itu.aviation.entity.Devise;
import com.itu.aviation.service.ClasseService;
import com.itu.aviation.service.DeviseService;
import com.itu.aviation.repository.AeroportRepository;
import com.itu.aviation.repository.ModeleAvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfigMvcController {

    @Autowired
    private AeroportRepository aeroportRepository;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private ModeleAvionRepository modeleAvionRepository;

    @Autowired
    private DeviseService deviseService;

    @GetMapping("/aeroports")
    public ModelAndView aeroports() {
        ModelAndView mav = new ModelAndView("config/aeroports");
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("pageTitle", "Aéroports");
        mav.addObject("pageDescription", "Gestion des aéroports");
        mav.addObject("currentPage", "aeroports");
        return mav;
    }

    @PostMapping("/aeroports/save")
    public String saveAeroport(@ModelAttribute Aeroport aeroport, RedirectAttributes redirect) {
        aeroportRepository.save(aeroport);
        redirect.addFlashAttribute("successMessage", "Aéroport enregistré");
        return "redirect:/aeroports";
    }

    @PostMapping("/aeroports/delete/{id}")
    public String deleteAeroport(@PathVariable Long id, RedirectAttributes redirect) {
        aeroportRepository.deleteById(id);
        redirect.addFlashAttribute("successMessage", "Aéroport supprimé");
        return "redirect:/aeroports";
    }

    @GetMapping("/classes")
    public ModelAndView classes() {
        ModelAndView mav = new ModelAndView("config/classes");
        mav.addObject("classes", classeService.findAll());
        mav.addObject("pageTitle", "Classes");
        mav.addObject("pageDescription", "Gestion des classes de voyage");
        mav.addObject("currentPage", "classes");
        return mav;
    }

    @PostMapping("/config/classes/save")
    public String saveClasse(@ModelAttribute Classe classe, RedirectAttributes redirect) {
        classeService.create(classe);
        redirect.addFlashAttribute("successMessage", "Classe enregistrée");
        return "redirect:/classes";
    }

    @PostMapping("/config/classes/delete/{id}")
    public String deleteClasse(@PathVariable Long id, RedirectAttributes redirect) {
        classeService.delete(id);
        redirect.addFlashAttribute("successMessage", "Classe supprimée");
        return "redirect:/classes";
    }

    @GetMapping("/modeles-avion")
    public ModelAndView modeles() {
        ModelAndView mav = new ModelAndView("config/modeles");
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("pageTitle", "Modèles Avion");
        mav.addObject("currentPage", "modeles");
        return mav;
    }

    @PostMapping("/modeles-avion/save")
    public String saveModele(@ModelAttribute ModeleAvion modele, RedirectAttributes redirect) {
        modeleAvionRepository.save(modele);
        redirect.addFlashAttribute("successMessage", "Modèle enregistré");
        return "redirect:/modeles-avion";
    }

    @GetMapping("/devises")
    public ModelAndView devises() {
        ModelAndView mav = new ModelAndView("config/devises");
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("pageTitle", "Devises");
        mav.addObject("currentPage", "devises");
        return mav;
    }

    @PostMapping("/devises/save")
    public String saveDevise(@ModelAttribute Devise devise, RedirectAttributes redirect) {
        deviseService.create(devise);
        redirect.addFlashAttribute("successMessage", "Devise enregistrée");
        return "redirect:/devises";
    }
}
