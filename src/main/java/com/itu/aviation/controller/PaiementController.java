package com.itu.aviation.controller;

import com.itu.aviation.entity.Paiement;
import com.itu.aviation.service.PaiementService;
import com.itu.aviation.service.ReservationService;
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
    
    @Autowired
    ReservationService reservationService;

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

    @GetMapping("/create")
    public ModelAndView create(@RequestParam Long reservationId) {
        ModelAndView mav = new ModelAndView("paiements/pay");
        mav.addObject("pageTitle", "Payer Billet");
        mav.addObject("currentPage", "paiements");
        mav.addObject("reservation", reservationService.findById(reservationId));
        mav.addObject("billets", billetService.findAll());
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("methodes", methodePaiementService.findAll());
        mav.addObject("statusList", statusPaiementService.findAll());
        return mav;
    }

    @PostMapping("/save-multiple")
    public String saveMultiple(@RequestParam Long reservationId,
                               @RequestParam(name = "methodeId") java.util.List<Long> methodeIds,
                               @RequestParam(name = "deviseId") java.util.List<Long> deviseIds,
                               @RequestParam(name = "montant") java.util.List<java.math.BigDecimal> montants,
                               @RequestParam(name = "statusId", required = false) java.util.List<Long> statusIds,
                               RedirectAttributes redirect) {
        try {
            com.itu.aviation.entity.Reservation res = reservationService.findById(reservationId);
            for (int i = 0; i < montants.size(); i++) {
                java.math.BigDecimal m = montants.get(i);
                if (m == null || m.compareTo(java.math.BigDecimal.ZERO) <= 0) continue;
                Paiement p = new Paiement();
                p.setReservation(res);
                p.setMontant(m);
                if (i < methodeIds.size() && methodeIds.get(i) != null) {
                    p.setMethodePaiement(methodePaiementService.findById(methodeIds.get(i)));
                }
                if (i < deviseIds.size() && deviseIds.get(i) != null) {
                    p.setDevise(deviseService.findById(deviseIds.get(i)));
                }
                if (statusIds != null && i < statusIds.size() && statusIds.get(i) != null) {
                    p.setStatusPaiement(statusPaiementService.findById(statusIds.get(i)));
                } else {
                    // default to first status if available
                    java.util.List<com.itu.aviation.entity.StatusPaiement> sl = statusPaiementService.findAll();
                    if (!sl.isEmpty()) p.setStatusPaiement(sl.get(0));
                }
                p.setDatePaiement(java.time.LocalDateTime.now());
                paiementService.create(p);
            }
            redirect.addFlashAttribute("successMessage", "Paiement(s) enregistré(s)");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors du paiement: " + ex.getMessage());
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
