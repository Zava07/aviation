
package com.itu.aviation.controller;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.service.BilletService;
import com.itu.aviation.service.ReservationService;
import com.itu.aviation.repository.BilletRepository;
import com.itu.aviation.repository.StatusBilletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/billets")
public class BilletController {

    @Autowired
    private BilletService billetService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StatusBilletRepository statusBilletRepository;

    @Autowired
    private BilletRepository billetRepository;

    @Autowired
    private com.itu.aviation.service.VolInstanceService volInstanceService;
    
    @Autowired
    private com.itu.aviation.service.PassagerService passagerService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String numero,
            @RequestParam(required = false) String passager,
            @RequestParam(required = false) String vol,
            @RequestParam(required = false) Long status,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateEmission) {
        ModelAndView mav = new ModelAndView("billets/list");
        mav.addObject("pageTitle", "Billets");
        mav.addObject("currentPage", "billets");
        java.util.List<com.itu.aviation.entity.Billet> billets = billetRepository.findAll();

        // apply filters if provided
        boolean hasFilter = (numero != null && !numero.isBlank()) || (passager != null && !passager.isBlank()) || (vol != null && !vol.isBlank()) || (status != null) || (dateEmission != null);
        if (hasFilter) {
            java.time.LocalDate d = dateEmission;
            billets = billets.stream().filter(b -> {
                if (numero != null && !numero.isBlank()) {
                    if (b.getNumeroBillet() == null || !b.getNumeroBillet().toLowerCase().contains(numero.toLowerCase())) return false;
                }
                if (passager != null && !passager.isBlank()) {
                    if (b.getReservation() == null || b.getReservation().getPassager() == null) return false;
                    String full = (b.getReservation().getPassager().getNom() == null ? "" : b.getReservation().getPassager().getNom()) + " " + (b.getReservation().getPassager().getPrenom() == null ? "" : b.getReservation().getPassager().getPrenom());
                    if (!full.toLowerCase().contains(passager.toLowerCase())) return false;
                }
                if (vol != null && !vol.isBlank()) {
                    if (b.getReservation() == null || b.getReservation().getVol() == null || b.getReservation().getVol().getVol() == null) return false;
                    if (b.getReservation().getVol().getVol().getNumeroVol() == null || !b.getReservation().getVol().getVol().getNumeroVol().toLowerCase().contains(vol.toLowerCase())) return false;
                }
                if (status != null) {
                    if (b.getStatusBillet() == null || b.getStatusBillet().getId() == null || !b.getStatusBillet().getId().equals(status)) return false;
                }
                if (d != null) {
                    if (b.getDateEmission() == null || !b.getDateEmission().toLocalDate().equals(d)) return false;
                }
                return true;
            }).collect(java.util.stream.Collectors.toList());
        }

        mav.addObject("billets", billets);

        // expose filter values back to view
        mav.addObject("filterNumero", numero);
        mav.addObject("filterPassager", passager);
        mav.addObject("filterVol", vol);
        mav.addObject("filterStatus", status);
        mav.addObject("filterDate", dateEmission);

        // revenue per VolInstance from billets
        java.util.Map<Long, java.math.BigDecimal> revenueMap = new java.util.HashMap<>();
        for (com.itu.aviation.entity.Billet b : billets) {
            if (b.getReservation() != null && b.getReservation().getVol() != null && b.getPrixFinal() != null) {
                Long vid = b.getReservation().getVol().getId();
                revenueMap.put(vid, revenueMap.getOrDefault(vid, java.math.BigDecimal.ZERO).add(b.getPrixFinal()));
            }
        }
        java.util.List<java.util.Map<String, Object>> revenueSummaries = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Long, java.math.BigDecimal> e : revenueMap.entrySet()) {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            Long vid = e.getKey();
            com.itu.aviation.entity.VolInstance vi = null;
            try { vi = volInstanceService.findById(vid); } catch (Exception ex) { /* ignore */ }
            String label = (vi != null && vi.getVol() != null) ? (vi.getVol().getNumeroVol() + " - " + vi.getDateDepart()) : ("VolInstance " + vid);
            m.put("label", label);
            m.put("total", e.getValue());
            revenueSummaries.add(m);
        }
        mav.addObject("revenueSummaries", revenueSummaries);
        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("vols", volInstanceService.findAll());
        mav.addObject("statusList", statusBilletRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("billets/list");
        mav.addObject("pageTitle", "Billets");
        mav.addObject("currentPage", "billets");
        mav.addObject("billets", billetService.findAll());
        mav.addObject("reservations", reservationService.findAll());
        mav.addObject("statusList", statusBilletRepository.findAll());
        mav.addObject("editBillet", billetService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Billet billet, RedirectAttributes redirect) {
        if (billet.getId() == null) {
            billetService.create(billet);
            redirect.addFlashAttribute("successMessage", "Billet créé");
        } else {
            billetService.update(billet.getId(), billet);
            redirect.addFlashAttribute("successMessage", "Billet mis à jour");
        }
        return "redirect:/billets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        billetService.delete(id);
        redirect.addFlashAttribute("successMessage", "Billet supprimé");
        return "redirect:/billets";
    }
}
