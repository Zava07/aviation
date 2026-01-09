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

    // Removed StatusVolRepository as it is no longer needed for route-level Vol entity.

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String date,
                             @RequestParam(required = false) Long destinationId,
                             @RequestParam(required = false) Long statusId) { // statusId parameter removed
        ModelAndView mav = new ModelAndView("vols/list");
        mav.addObject("pageTitle", "Vols");
        mav.addObject("currentPage", "vols");
        List<com.itu.aviation.entity.Vol> vols = volService.findAll();
        // simple filtering in memory (can be optimized)
        if (destinationId != null) {
            vols = vols.stream().filter(v -> v.getAeroportDestination() != null && v.getAeroportDestination().getId().equals(destinationId)).toList();
        }

        mav.addObject("vols", vols);
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        // Removed statusList as it is no longer needed for route-level Vol entity.
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("vols/form");
        mav.addObject("pageTitle", "Modifier Vol");
        mav.addObject("currentPage", "vols");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        // Removed statusList as it is no longer needed for route-level Vol entity.
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
        // Removed statusList as it is no longer needed for route-level Vol entity.
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Vol vol, RedirectAttributes redirect) {

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

    // Statuses are managed at VolInstance (scheduling) level; this endpoint removed.

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        volService.delete(id);
        redirect.addFlashAttribute("successMessage", "Vol supprimé");
        return "redirect:/vols";
    }
}
