package com.itu.aviation.controller;

import com.itu.aviation.entity.Aeroport;
import com.itu.aviation.repository.AeroportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/aeroports")
public class AeroportController {

    @Autowired
    private AeroportRepository aeroportRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String nom) {
        ModelAndView mav = new ModelAndView("aeroports/list");
        mav.addObject("pageTitle", "Aéroports");
        mav.addObject("currentPage", "aeroports");
        java.util.List<com.itu.aviation.entity.Aeroport> all = aeroportRepository.findAll();
        java.util.List<com.itu.aviation.entity.Aeroport> filtered = all.stream().filter(a -> {
            if (nom != null && !nom.isBlank()) {
                if (a.getNomAeroport() == null || !a.getNomAeroport().toLowerCase().contains(nom.toLowerCase())) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("aeroports", filtered);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("aeroports/form");
        mav.addObject("pageTitle", "Nouveau Aéroport");
        mav.addObject("currentPage", "aeroports");
        mav.addObject("editAeroport", new Aeroport());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("aeroports/form");
        mav.addObject("pageTitle", "Modifier Aéroport");
        mav.addObject("currentPage", "aeroports");
        mav.addObject("editAeroport", aeroportRepository.findById(id).orElse(new Aeroport()));
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute Aeroport aeroport, RedirectAttributes redirect) {
        aeroportRepository.save(aeroport);
        redirect.addFlashAttribute("successMessage", "Aéroport enregistré");
        return new ModelAndView("redirect:/aeroports");
    }

    @PostMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        aeroportRepository.deleteById(id);
        redirect.addFlashAttribute("successMessage", "Aéroport supprimé");
        return new ModelAndView("redirect:/aeroports");
    }
}
