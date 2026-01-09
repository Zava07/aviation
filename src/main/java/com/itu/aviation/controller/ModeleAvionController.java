package com.itu.aviation.controller;

import com.itu.aviation.entity.ModeleAvion;
import com.itu.aviation.service.ModeleAvionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/modeles")
public class ModeleAvionController {

    @Autowired
    private ModeleAvionService modeleAvionService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("modeles/list");
        mav.addObject("pageTitle", "Modèles Avion");
        mav.addObject("currentPage", "modeles");
        mav.addObject("modeles", modeleAvionService.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("modeles/form");
        mav.addObject("pageTitle", "Nouveau Modèle");
        mav.addObject("currentPage", "modeles");
        mav.addObject("editModele", new ModeleAvion());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("modeles/form");
        mav.addObject("pageTitle", "Modifier Modèle");
        mav.addObject("currentPage", "modeles");
        mav.addObject("editModele", modeleAvionService.findById(id));
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute ModeleAvion m, RedirectAttributes redirect) {
        modeleAvionService.create(m);
        redirect.addFlashAttribute("successMessage", "Modèle enregistré");
        return new ModelAndView("redirect:/modeles");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        modeleAvionService.delete(id);
        redirect.addFlashAttribute("successMessage", "Modèle supprimé");
        return new ModelAndView("redirect:/modeles");
    }
}
