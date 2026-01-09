package com.itu.aviation.controller;

import com.itu.aviation.entity.Passager;
import com.itu.aviation.service.PassagerService;
import com.itu.aviation.repository.TypePassagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/passagers")
public class PassagerMvcController {

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private TypePassagerRepository typePassagerRepository;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("passagers/list");
        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("types", typePassagerRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("passagers/list");
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
