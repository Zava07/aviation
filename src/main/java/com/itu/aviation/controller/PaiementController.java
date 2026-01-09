package com.itu.aviation.controller;

import com.itu.aviation.entity.Paiement;
import com.itu.aviation.service.PaiementService;
import com.itu.aviation.service.BilletService;
import com.itu.aviation.service.DeviseService;
import com.itu.aviation.service.MethodePaiementService;
import com.itu.aviation.service.StatusPaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private BilletService billetService;

    @Autowired
    private DeviseService deviseService;

    @Autowired
    private MethodePaiementService methodePaiementService;

    @Autowired
    private StatusPaiementService statusPaiementService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) Long reservationId,
                             @RequestParam(required = false) Long statusId,
                             @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateFrom,
                             @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateTo) {
        ModelAndView mav = new ModelAndView("paiements/list");
        mav.addObject("pageTitle", "Paiements");
        mav.addObject("currentPage", "paiements");
        java.util.List<com.itu.aviation.entity.Paiement> all = paiementService.findAll();
        java.util.List<com.itu.aviation.entity.Paiement> filtered = all.stream().filter(p -> {
            if (reservationId != null) {
                if (p.getReservation() == null || !reservationId.equals(p.getReservation().getId())) return false;
            }
            if (statusId != null) {
                if (p.getStatusPaiement() == null || !statusId.equals(p.getStatusPaiement().getId())) return false;
            }
            if (dateFrom != null) {
                if (p.getDatePaiement() == null || p.getDatePaiement().toLocalDate().isBefore(dateFrom)) return false;
            }
            if (dateTo != null) {
                if (p.getDatePaiement() == null || p.getDatePaiement().toLocalDate().isAfter(dateTo)) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("paiements", filtered);
        mav.addObject("billets", billetService.findAll());
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("methodes", methodePaiementService.findAll());
        mav.addObject("statusList", statusPaiementService.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("paiements/list");
        mav.addObject("pageTitle", "Paiements");
        mav.addObject("currentPage", "paiements");
        mav.addObject("paiements", paiementService.findAll());
        mav.addObject("billets", billetService.findAll());
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("methodes", methodePaiementService.findAll());
        mav.addObject("statusList", statusPaiementService.findAll());
        mav.addObject("editPaiement", paiementService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Paiement paiement, RedirectAttributes redirect) {
        if (paiement.getId() == null) {
            paiementService.create(paiement);
            redirect.addFlashAttribute("successMessage", "Paiement créé");
        } else {
            paiementService.update(paiement.getId(), paiement);
            redirect.addFlashAttribute("successMessage", "Paiement mis à jour");
        }
        return "redirect:/paiements";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        paiementService.delete(id);
        redirect.addFlashAttribute("successMessage", "Paiement supprimé");
        return "redirect:/paiements";
    }
}
