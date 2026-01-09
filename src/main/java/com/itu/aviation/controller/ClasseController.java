package com.itu.aviation.controller;

import com.itu.aviation.entity.Classe;
import com.itu.aviation.service.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 
@Controller
@RequestMapping("/classes")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String nomClasse,
                             @RequestParam(required = false) Integer niveau) {
        ModelAndView mav = new ModelAndView("classes/list");
        mav.addObject("pageTitle", "Classes");
        mav.addObject("currentPage", "classes");
        List<com.itu.aviation.entity.Classe> all = classeService.findAll();
        List<com.itu.aviation.entity.Classe> filtered = all.stream().filter(c -> {
            if (nomClasse != null && !nomClasse.isBlank()) {
                if (c.getNomClasse() == null || !c.getNomClasse().toLowerCase().contains(nomClasse.toLowerCase())) return false;
            }
            if (niveau != null) {
                if (c.getNiveau() == null || !niveau.equals(c.getNiveau())) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("classes", filtered);
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
