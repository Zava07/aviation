package com.itu.aviation.controller;

import com.itu.aviation.entity.Siege;
import com.itu.aviation.service.SiegeService;
import com.itu.aviation.service.AvionService;
import com.itu.aviation.service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sieges")
public class SiegeController {

    @Autowired
    private SiegeService siegeService;

    @Autowired
    private AvionService avionService;

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("sieges/list");
        mav.addObject("pageTitle", "Sièges");
        mav.addObject("currentPage", "sieges");
        mav.addObject("sieges", siegeService.findAll());
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("sieges/form");
        mav.addObject("pageTitle", "Nouveau Siège");
        mav.addObject("currentPage", "sieges");
        mav.addObject("editSiege", new Siege());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("classes", classeService.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("sieges/form");
        mav.addObject("pageTitle", "Modifier Siège");
        mav.addObject("currentPage", "sieges");
        mav.addObject("editSiege", siegeService.findById(id));
        mav.addObject("avions", avionService.findAll());
        mav.addObject("classes", classeService.findAll());
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute Siege s, RedirectAttributes redirect) {
        siegeService.create(s);
        redirect.addFlashAttribute("successMessage", "Siège enregistré");
        return new ModelAndView("redirect:/sieges");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        siegeService.delete(id);
        redirect.addFlashAttribute("successMessage", "Siège supprimé");
        return new ModelAndView("redirect:/sieges");
    }
}
