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
import java.util.List;
import java.util.stream.Collectors;
 

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
    public ModelAndView list(@RequestParam(required = false) Long volId,
                             @RequestParam(required = false) Long classeId,
                             @RequestParam(required = false) java.math.BigDecimal minPrice,
                             @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        ModelAndView mav = new ModelAndView("prix/list");
        mav.addObject("pageTitle", "Prix Vol/Classe");
        mav.addObject("currentPage", "prix");
        java.util.List<com.itu.aviation.entity.PrixVolClasse> all = prixService.findAll();
        java.util.List<com.itu.aviation.entity.PrixVolClasse> filtered = all.stream().filter(p -> {
            if (volId != null) {
                if (p.getVol() == null || !volId.equals(p.getVol().getId())) return false;
            }
            if (classeId != null) {
                if (p.getClasse() == null || !classeId.equals(p.getClasse().getId())) return false;
            }
            if (minPrice != null) {
                if (p.getPrix() == null || p.getPrix().compareTo(minPrice) < 0) return false;
            }
            if (maxPrice != null) {
                if (p.getPrix() == null || p.getPrix().compareTo(maxPrice) > 0) return false;
            }
            return true;
        }).collect(java.util.stream.Collectors.toList());
        mav.addObject("prixList", filtered);
        mav.addObject("vols", volService.findAll());
        mav.addObject("classes", classeService.findAll());
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
