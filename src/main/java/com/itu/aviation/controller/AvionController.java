package com.itu.aviation.controller;

import com.itu.aviation.entity.Avion;
import com.itu.aviation.service.AvionService;
import com.itu.aviation.repository.ModeleAvionRepository;
import com.itu.aviation.repository.EtatAvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/avions")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @Autowired
    private ModeleAvionRepository modeleAvionRepository;

    @Autowired
    private EtatAvionRepository etatAvionRepository;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("avions/list");
        mav.addObject("pageTitle", "Avions");
        mav.addObject("currentPage", "avions");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("avions/form");
        mav.addObject("pageTitle", "Modifier Avion");
        mav.addObject("currentPage", "avions");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        mav.addObject("editAvion", avionService.findById(id));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("avions/form");
        mav.addObject("pageTitle", "Nouveau Avion");
        mav.addObject("currentPage", "avions");
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        mav.addObject("editAvion", new Avion());
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute Avion avion, RedirectAttributes redirect) {
        if (avion.getId() == null) {
            avionService.create(avion);
            redirect.addFlashAttribute("successMessage", "Avion créé");
        } else {
            avionService.update(avion.getId(), avion);
            redirect.addFlashAttribute("successMessage", "Avion mis à jour");
        }
        return new ModelAndView("redirect:/avions");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        avionService.delete(id);
        redirect.addFlashAttribute("errorMessage", "Avion supprimé");
        return new ModelAndView("redirect:/avions");
    }
}
