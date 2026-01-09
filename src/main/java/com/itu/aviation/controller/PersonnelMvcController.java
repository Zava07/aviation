package com.itu.aviation.controller;

import com.itu.aviation.entity.MembreEquipe;
import com.itu.aviation.service.MembreEquipeService;
import com.itu.aviation.repository.TypeMembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/personnel")
public class PersonnelMvcController {

    @Autowired
    private MembreEquipeService membreEquipeService;

    @Autowired
    private TypeMembreRepository typeMembreRepository;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("personnel/list");
        mav.addObject("membres", membreEquipeService.findAll());
        mav.addObject("types", typeMembreRepository.findAll());
        return mav;
    }

    // route alternative used by layout
    @GetMapping("/equipages")
    public String equipages() {
        return "redirect:/personnel";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("personnel/list");
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
