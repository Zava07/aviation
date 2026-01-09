package com.itu.aviation.controller;

import com.itu.aviation.entity.Vol;
import com.itu.aviation.service.VolService;
import com.itu.aviation.service.AvionService;
import com.itu.aviation.repository.AeroportRepository;
import com.itu.aviation.repository.StatusVolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vols")
public class VolMvcController {

    @Autowired
    private VolService volService;

    @Autowired
    private AvionService avionService;

    @Autowired
    private AeroportRepository aeroportRepository;

    @Autowired
    private StatusVolRepository statusVolRepository;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("vols/list");
        mav.addObject("vols", volService.findAll());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("vols/list");
        mav.addObject("vols", volService.findAll());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        mav.addObject("editVol", volService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Vol vol, RedirectAttributes redirect) {
        if (vol.getId() == null) {
            volService.create(vol);
            redirect.addFlashAttribute("successMessage", "Vol créé");
        } else {
            volService.update(vol.getId(), vol);
            redirect.addFlashAttribute("successMessage", "Vol mis à jour");
        }
        return "redirect:/vols";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        volService.delete(id);
        redirect.addFlashAttribute("successMessage", "Vol supprimé");
        return "redirect:/vols";
    }
}
