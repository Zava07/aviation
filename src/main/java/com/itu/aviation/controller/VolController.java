package com.itu.aviation.controller;

import com.itu.aviation.entity.Vol;
import com.itu.aviation.service.VolService;
import com.itu.aviation.service.AvionService;
import com.itu.aviation.repository.AeroportRepository;
import com.itu.aviation.repository.StatusVolRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vols")
public class VolController {

    @Autowired
    private VolService volService;

    @Autowired
    private AvionService avionService;

    @Autowired
    private AeroportRepository aeroportRepository;

    @Autowired
    private StatusVolRepository statusVolRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String date,
                             @RequestParam(required = false) Long destinationId,
                             @RequestParam(required = false) Long statusId) {
        ModelAndView mav = new ModelAndView("vols/list");
        mav.addObject("pageTitle", "Vols");
        mav.addObject("currentPage", "vols");
        List<com.itu.aviation.entity.Vol> vols = volService.findAll();
        // simple filtering in memory (can be optimized)
        if (date != null && !date.isBlank()) {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            vols = vols.stream().filter(v -> v.getDateDepart().toLocalDate().equals(d)).toList();
        }
        if (destinationId != null) {
            vols = vols.stream().filter(v -> v.getAeroportDestination() != null && v.getAeroportDestination().getId().equals(destinationId)).toList();
        }
        if (statusId != null) {
            vols = vols.stream().filter(v -> v.getStatusVol() != null && v.getStatusVol().getId().equals(statusId)).toList();
        }

        mav.addObject("vols", vols);
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("vols/form");
        mav.addObject("pageTitle", "Modifier Vol");
        mav.addObject("currentPage", "vols");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        mav.addObject("editVol", volService.findById(id));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("vols/form");
        mav.addObject("pageTitle", "Nouveau Vol");
        mav.addObject("currentPage", "vols");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Vol vol, RedirectAttributes redirect) {
        // ensure a status is set (statusVol is non-nullable)
        if (vol.getStatusVol() == null) {
            com.itu.aviation.entity.StatusVol def = statusVolRepository.findAll().stream().findFirst().orElse(null);
            if (def == null) {
                redirect.addFlashAttribute("errorMessage", "Aucun statut de vol n'est configuré. Veuillez créer un statut avant d'ajouter un vol.");
                return "redirect:/vols/create";
            }
            vol.setStatusVol(def);
        }

        try {
            if (vol.getId() == null) {
                volService.create(vol);
                redirect.addFlashAttribute("successMessage", "Vol créé");
            } else {
                volService.update(vol.getId(), vol);
                redirect.addFlashAttribute("successMessage", "Vol mis à jour");
            }
        } catch (Exception ex) {
            // provide a friendlier message and avoid server 500 stacktrace to user
            redirect.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement du vol: " + ex.getMessage());
        }
        return "redirect:/vols";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam Long statusId, RedirectAttributes redirect) {
        com.itu.aviation.entity.Vol vol = volService.findById(id);
        com.itu.aviation.entity.StatusVol status = statusVolRepository.findById(statusId).orElse(null);
        if (status == null) {
            redirect.addFlashAttribute("errorMessage", "Statut invalide");
            return "redirect:/vols";
        }
        vol.setStatusVol(status);
        volService.update(id, vol);
        redirect.addFlashAttribute("successMessage", "Statut mis à jour");
        return "redirect:/vols";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        volService.delete(id);
        redirect.addFlashAttribute("successMessage", "Vol supprimé");
        return "redirect:/vols";
    }
}
