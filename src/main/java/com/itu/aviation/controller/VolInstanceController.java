package com.itu.aviation.controller;

import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.service.VolInstanceService;
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
@RequestMapping("/vol-instances")
public class VolInstanceController {

    @Autowired
    private VolInstanceService volInstanceService;

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
        ModelAndView mav = new ModelAndView("vols/instances/list");
        mav.addObject("pageTitle", "Instances de vol");
        mav.addObject("currentPage", "vol-instances");
        List<VolInstance> vols = volInstanceService.findAll();
        if (date != null && !date.isBlank()) {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            vols = vols.stream().filter(v -> v.getDateDepart().toLocalDate().equals(d)).toList();
        }
        if (destinationId != null) {
            vols = vols.stream().filter(v -> v.getVol()!=null && v.getVol().getAeroportDestination()!=null && v.getVol().getAeroportDestination().getId().equals(destinationId)).toList();
        }
        if (statusId != null) {
            vols = vols.stream().filter(v -> v.getStatusVol() != null && v.getStatusVol().getId().equals(statusId)).toList();
        }

        mav.addObject("vols", vols);
        mav.addObject("volRoutes", volService.findAll());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("vols/instances/form");
        mav.addObject("pageTitle", "Modifier Instance de Vol");
        mav.addObject("currentPage", "vol-instances");
        mav.addObject("volRoutes", volService.findAll());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        mav.addObject("editInstance", volInstanceService.findById(id));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("vols/instances/form");
        mav.addObject("pageTitle", "Nouvelle Instance de Vol");
        mav.addObject("currentPage", "vol-instances");
        mav.addObject("volRoutes", volService.findAll());
        mav.addObject("avions", avionService.findAll());
        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("statusList", statusVolRepository.findAll());
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute VolInstance vi, RedirectAttributes redirect) {
        try {
            if (vi.getId() == null) {
                if(vi.getDateDepart().isAfter(vi.getDateArrivee())){
                    redirect.addFlashAttribute("errorMessage", "La date de départ doit être avant la date d'arrivée.");
                    return "redirect:/vol-instances/create";
                }
                List<VolInstance> existingInstances = volInstanceService.findByAvionAndVol(
                        vi.getAvion() != null ? vi.getAvion().getId() : null,
                        vi.getVol() != null ? vi.getVol().getId() : null
                );

                if(!existingInstances.isEmpty()){
                    for(VolInstance existingInstance : existingInstances){
                        if(vi.getDateDepart().isBefore(existingInstance.getDateArrivee()) &&
                           vi.getDateArrivee().isAfter(existingInstance.getDateDepart())){
                            redirect.addFlashAttribute("errorMessage", "L'avion est déjà affecté à une autre instance de vol pendant cette période pour ce vol.");
                            return "redirect:/vol-instances/create";
                        }
                    }
                }
                volInstanceService.create(vi);
                redirect.addFlashAttribute("successMessage", "Instance de vol créée");
            } else {
                if(vi.getDateDepart().isAfter(vi.getDateArrivee())){
                    redirect.addFlashAttribute("errorMessage", "La date de départ doit être avant la date d'arrivée.");
                    return "redirect:/vol-instances/create";
                }
                List<VolInstance> existingInstances = volInstanceService.findByAvionAndVol(
                        vi.getAvion() != null ? vi.getAvion().getId() : null,
                        vi.getVol() != null ? vi.getVol().getId() : null
                );

                if(!existingInstances.isEmpty()){
                    for(VolInstance existingInstance : existingInstances){
                        if(vi.getDateDepart().isBefore(existingInstance.getDateArrivee()) &&
                           vi.getDateArrivee().isAfter(existingInstance.getDateDepart())){
                            redirect.addFlashAttribute("errorMessage", "L'avion est déjà affecté à une autre instance de vol pendant cette période pour ce vol.");
                            return "redirect:/vol-instances/create";
                        }
                    }
                }
                volInstanceService.update(vi.getId(), vi);
                redirect.addFlashAttribute("successMessage", "Instance de vol mise à jour");
            }
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement de l'instance: " + ex.getMessage());
        }
        return "redirect:/vol-instances";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        volInstanceService.delete(id);
        redirect.addFlashAttribute("successMessage", "Instance supprimée");
        return "redirect:/vol-instances";
    }

    // Admin utility to initialize missing siege_vol rows
    @PostMapping("/init-seats")
    public String initSeats(RedirectAttributes redirect) {
        try {
            int created = volInstanceService.initializeMissingSiegeVolsForAll();
            redirect.addFlashAttribute("successMessage", "Initialisation terminée. SiegeVols créés: " + created);
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur initialisation SiegeVols: " + ex.getMessage());
        }
        return "redirect:/vol-instances";
    }
}
