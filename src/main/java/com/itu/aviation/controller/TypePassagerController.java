package com.itu.aviation.controller;

import com.itu.aviation.entity.TypePassager;
import com.itu.aviation.repository.TypePassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/type-passagers")
public class TypePassagerController {

    @Autowired
    private TypePassagerRepository repo;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("typepassagers/list");
        mav.addObject("pageTitle", "Types Passagers");
        mav.addObject("currentPage", "type-passagers");
        mav.addObject("items", repo.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("typepassagers/form");
        mav.addObject("pageTitle", "Nouveau Type Passager");
        mav.addObject("currentPage", "type-passagers");
        mav.addObject("item", new TypePassager());
        mav.addObject("action", "/type-passagers/save");
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("typepassagers/form");
        mav.addObject("pageTitle", "Modifier Type Passager");
        mav.addObject("currentPage", "type-passagers");
        mav.addObject("item", repo.findById(id).orElse(new TypePassager()));
        mav.addObject("action", "/type-passagers/save");
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute TypePassager t, RedirectAttributes redirect) {
        repo.save(t);
        redirect.addFlashAttribute("successMessage", "Type enregistré");
        return new ModelAndView("redirect:/type-passagers");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        repo.deleteById(id);
        redirect.addFlashAttribute("successMessage", "Type supprimé");
        return new ModelAndView("redirect:/type-passagers");
    }
}
