package com.itu.aviation.controller;

import com.itu.aviation.entity.Avion;
import com.itu.aviation.entity.Classe;
import com.itu.aviation.entity.Siege;

import com.itu.aviation.repository.ModeleAvionRepository;
import com.itu.aviation.repository.EtatAvionRepository;

import com.itu.aviation.service.AvionService;
import com.itu.aviation.service.ClasseService;
import com.itu.aviation.service.SiegeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/avions")
public class AvionController {

    @Autowired
    private AvionService avionService;

    @Autowired
    private ModeleAvionRepository modeleAvionRepository;

    @Autowired
    private EtatAvionRepository etatAvionRepository;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private SiegeService siegeService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("avions/list");
        mav.addObject("pageTitle", "Avions");
        mav.addObject("currentPage", "avions");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("avions/form");
        mav.addObject("pageTitle", "Modifier Avion");
        mav.addObject("currentPage", "avions");
        mav.addObject("avions", avionService.findAll());
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        mav.addObject("classes", classeService.findAll());
        mav.addObject("editAvion", avionService.findById(id));
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("avions/form");
        mav.addObject("pageTitle", "Nouveau Avion");
        mav.addObject("currentPage", "avions");
        mav.addObject("modeles", modeleAvionRepository.findAll());
        mav.addObject("etats", etatAvionRepository.findAll());
        mav.addObject("classes", classeService.findAll());
        mav.addObject("editAvion", new Avion());
        return mav;
    }

    @PostMapping("/save")
    public ModelAndView save(@ModelAttribute Avion avion, RedirectAttributes redirect,HttpServletRequest request) {
        boolean isNew = (avion.getId() == null);
        Avion saved = null;
        if (isNew) {
            saved = avionService.create(avion);
            redirect.addFlashAttribute("successMessage", "Avion créé");
        } else {
            saved = avionService.update(avion.getId(), avion);
            redirect.addFlashAttribute("successMessage", "Avion mis à jour");
        }

        // If newly created, create seats according to classes inputs
        if (isNew) {
            java.util.List<com.itu.aviation.entity.Classe> classes = classeService.findAll();
            int totalSpecified = 0;
            java.util.Map<Long, Integer> counts = new java.util.HashMap<>();
            for (com.itu.aviation.entity.Classe c : classes) {
                String param = request.getParameter("seatsByClass_" + c.getId());
                int val = 0;
                if (param != null && !param.isBlank()) {
                    try { val = Integer.parseInt(param); } catch (NumberFormatException ex) { val = 0; }
                }
                if (val > 0) {
                    counts.put(c.getId(), val);
                    totalSpecified += val;
                }
            }

            int capacity = avion.getCapacitePassagers() != null ? avion.getCapacitePassagers() : 0;
            if (totalSpecified != capacity) {
                redirect.addFlashAttribute("errorMessage", "La somme des sièges par classe doit être égale à la capacité totale (" + capacity + ") : spécifié=" + totalSpecified);
                // delete the created avion to avoid orphan
                try { avionService.delete(saved.getId()); } catch (Exception ex) { }
                return new ModelAndView("redirect:/avions/create");
            }

            // create sieges
            for (Classe c : classes) {
                Integer cnt = counts.get(c.getId());
                if (cnt == null || cnt <= 0) continue;
                for (int i = 1; i <= cnt; i++) {
                    Siege s = new Siege();
                    s.setAvion(saved);
                    s.setClasse(c);
                    // generate a seat number: class code (or id) + index
                    String prefix;
                    if (c.getNomClasse() != null && !c.getNomClasse().isBlank()) {
                        prefix = c.getNomClasse().length() > 3 ? c.getNomClasse().substring(0,3).toUpperCase() : c.getNomClasse().toUpperCase();
                    } else {
                        prefix = "C" + c.getId();
                    }
                    String numero = prefix + "-" + i;
                    if (numero.length() > 10) numero = numero.substring(0, 10);
                    s.setNumero(numero);
                    try { siegeService.create(s); } catch (Exception ex) { /* continue */ }
                }
            }
        }

        return new ModelAndView("redirect:/avions");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id, RedirectAttributes redirect) {
        avionService.delete(id);
        redirect.addFlashAttribute("errorMessage", "Avion supprimé");
        return new ModelAndView("redirect:/avions");
    }
}
