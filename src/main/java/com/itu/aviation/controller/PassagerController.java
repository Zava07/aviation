package com.itu.aviation.controller;

import com.itu.aviation.entity.Passager;
import com.itu.aviation.service.PassagerService;
import com.itu.aviation.repository.TypePassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/passagers")
public class PassagerController {

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private TypePassagerRepository typePassagerRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String nom,
                             @RequestParam(required = false) String prenom,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) Long typeId) {
        ModelAndView mav = new ModelAndView("passagers/list");
        mav.addObject("pageTitle", "Passagers");
        mav.addObject("currentPage", "passagers");
        List<com.itu.aviation.entity.Passager> all = passagerService.findAll();
        List<com.itu.aviation.entity.Passager> filtered = all.stream().filter(p -> {
            if (nom != null && !nom.isBlank()) {
                if (p.getNom() == null || !p.getNom().toLowerCase().contains(nom.toLowerCase())) return false;
            }
            if (prenom != null && !prenom.isBlank()) {
                if (p.getPrenom() == null || !p.getPrenom().toLowerCase().contains(prenom.toLowerCase())) return false;
            }
            if (email != null && !email.isBlank()) {
                if (p.getEmail() == null || !p.getEmail().toLowerCase().contains(email.toLowerCase())) return false;
            }
            if (typeId != null) {
                if (p.getTypePassager() == null || !typeId.equals(p.getTypePassager().getId())) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("passagers", filtered);
        mav.addObject("types", typePassagerRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("passagers/list");
        mav.addObject("pageTitle", "Passagers");
        mav.addObject("currentPage", "passagers");
        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("types", typePassagerRepository.findAll());
        mav.addObject("editPassager", passagerService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Passager passager, RedirectAttributes redirect) {
        if (passager.getId() == null) {
            passagerService.create(passager);
            redirect.addFlashAttribute("successMessage", "Passager créé");
        } else {
            passagerService.update(passager.getId(), passager);
            redirect.addFlashAttribute("successMessage", "Passager mis à jour");
        }
        return "redirect:/passagers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        passagerService.delete(id);
        redirect.addFlashAttribute("successMessage", "Passager supprimé");
        return "redirect:/passagers";
    }
}
