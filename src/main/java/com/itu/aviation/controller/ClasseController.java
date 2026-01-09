package com.itu.aviation.controller;

import com.itu.aviation.entity.Classe;
import com.itu.aviation.service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("classes/list");
        mav.addObject("pageTitle", "Classes");
        mav.addObject("currentPage", "classes");
        mav.addObject("classes", classeService.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("classes/form");
        mav.addObject("pageTitle", "Nouvelle Classe");
        mav.addObject("currentPage", "classes");
        mav.addObject("editClasse", new Classe());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("classes/form");
        mav.addObject("pageTitle", "Modifier Classe");
        mav.addObject("currentPage", "classes");
        mav.addObject("editClasse", classeService.findById(id));
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute Classe classe, RedirectAttributes redirect) {
        classeService.create(classe);
        redirect.addFlashAttribute("successMessage", "Classe enregistrée");
        return new ModelAndView("redirect:/classes");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        classeService.delete(id);
        redirect.addFlashAttribute("successMessage", "Classe supprimée");
        return new ModelAndView("redirect:/classes");
    }
}
