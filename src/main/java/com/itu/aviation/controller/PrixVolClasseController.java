package com.itu.aviation.controller;

import com.itu.aviation.entity.PrixVolClasse;
import com.itu.aviation.service.PrixVolClasseService;
import com.itu.aviation.service.VolService;
import com.itu.aviation.service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prix")
public class PrixVolClasseController {

    @Autowired
    private PrixVolClasseService prixService;

    @Autowired
    private VolService volService;

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("prix/list");
        mav.addObject("pageTitle", "Prix Vol/Classe");
        mav.addObject("currentPage", "prix");
        mav.addObject("prixList", prixService.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("prix/form");
        mav.addObject("pageTitle", "Nouveau Prix");
        mav.addObject("currentPage", "prix");
        mav.addObject("editPrix", new PrixVolClasse());
        mav.addObject("vols", volService.findAll());
        mav.addObject("classes", classeService.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("prix/form");
        mav.addObject("pageTitle", "Modifier Prix");
        mav.addObject("currentPage", "prix");
        mav.addObject("editPrix", prixService.findById(id));
        mav.addObject("vols", volService.findAll());
        mav.addObject("classes", classeService.findAll());
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute PrixVolClasse p, RedirectAttributes redirect) {
        prixService.create(p);
        redirect.addFlashAttribute("successMessage", "Prix enregistré");
        return new ModelAndView("redirect:/prix");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        prixService.delete(id);
        redirect.addFlashAttribute("successMessage", "Prix supprimé");
        return new ModelAndView("redirect:/prix");
    }
}
