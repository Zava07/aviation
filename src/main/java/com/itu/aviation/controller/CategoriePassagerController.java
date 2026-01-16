package com.itu.aviation.controller;

import com.itu.aviation.entity.CategoriePassager;
import com.itu.aviation.service.CategoriePassagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorie-passager")
public class CategoriePassagerController {

    @Autowired
    private CategoriePassagerService service;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("categoriepassager/list");
        mav.addObject("pageTitle", "Catégories passagers");
        mav.addObject("currentPage", "categorie-passager");
        mav.addObject("categories", service.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("categoriepassager/form");
        mav.addObject("pageTitle", "Nouvelle catégorie");
        mav.addObject("currentPage", "categorie-passager");
        mav.addObject("editCategorie", new CategoriePassager());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("categoriepassager/form");
        mav.addObject("pageTitle", "Modifier catégorie");
        mav.addObject("currentPage", "categorie-passager");
        mav.addObject("editCategorie", service.findById(id));
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute CategoriePassager c, RedirectAttributes redirect) {
        if (c.getId() == null) service.create(c);
        else service.update(c.getId(), c);
        redirect.addFlashAttribute("successMessage", "Catégorie enregistrée");
        return new ModelAndView("redirect:/categorie-passager");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        service.delete(id);
        redirect.addFlashAttribute("successMessage", "Catégorie supprimée");
        return new ModelAndView("redirect:/categorie-passager");
    }
}
