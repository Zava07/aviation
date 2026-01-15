package com.itu.aviation.controller;

import com.itu.aviation.entity.Config;
import com.itu.aviation.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/configurations")
public class ConfigurationController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("configurations/list");
        mav.addObject("pageTitle", "Configuration Système");
        mav.addObject("currentPage", "configurations");
        mav.addObject("configs", configService.findAll());
        mav.addObject("currentConfig", configService.findCurrentConfig().orElse(null));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("configurations/form");
        mav.addObject("pageTitle", "Nouvelle Configuration");
        mav.addObject("currentPage", "configurations");
        mav.addObject("config", new Config());
        mav.addObject("isEdit", false);
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("configurations/form");
        mav.addObject("pageTitle", "Modifier Configuration");
        mav.addObject("currentPage", "configurations");
        mav.addObject("config", configService.findById(id));
        mav.addObject("isEdit", true);
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Config config, RedirectAttributes redirect) {
        try {
            if (config.getId() == null) {
                configService.create(config);
                redirect.addFlashAttribute("successMessage", "Configuration créée avec succès");
            } else {
                configService.update(config.getId(), config);
                redirect.addFlashAttribute("successMessage", "Configuration mise à jour avec succès");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Erreur: " + e.getMessage());
        }
        return "redirect:/configurations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            configService.delete(id);
            redirect.addFlashAttribute("successMessage", "Configuration supprimée avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/configurations";
    }
}
