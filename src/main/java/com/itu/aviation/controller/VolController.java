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
    private com.itu.aviation.service.VolInstanceService volInstanceService;

    @Autowired
    private com.itu.aviation.service.ReservationService reservationService;

    @Autowired
    private com.itu.aviation.repository.BilletRepository billetRepository;

    @Autowired
    private com.itu.aviation.repository.PubliciteRepository publiciteRepository;

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

        // parse optional date filter (yyyy-MM-dd)
        java.time.LocalDate filterDate = null;
        if (date != null && !date.isBlank()) {
            try { filterDate = java.time.LocalDate.parse(date); } catch (Exception ex) { filterDate = null; }
            mav.addObject("filterDate", date);
        }
        final java.time.LocalDate filterDateRef = filterDate;

        // For each vol compute aggregated CA: tickets, publicity, total
        java.util.List<java.util.Map<String, Object>> volCaList = new java.util.ArrayList<>();
        for (com.itu.aviation.entity.Vol v : vols) {
            java.math.BigDecimal ticketsSum = java.math.BigDecimal.ZERO;
            java.math.BigDecimal adsSum = java.math.BigDecimal.ZERO;

            // find instances for this vol
            java.util.List<com.itu.aviation.entity.VolInstance> instances = volInstanceService.findAll().stream()
                    .filter(vi -> vi.getVol() != null && vi.getVol().getId() != null && vi.getVol().getId().equals(v.getId()))
                    .filter(vi -> {
                        if (filterDateRef == null) return true;
                        if (vi.getDateDepart() == null) return false;
                        return vi.getDateDepart().toLocalDate().equals(filterDateRef);
                    })
                    .toList();

            for (com.itu.aviation.entity.VolInstance vi : instances) {
                // tickets revenue similar to AvionController (sum of reservation.prix for reservations that have a paid/emis billet)
                try {
                    java.util.List<com.itu.aviation.entity.Reservation> allRes = reservationService.findAll();
                    for (com.itu.aviation.entity.Reservation rr : allRes) {
                        if (rr.getVol() != null && rr.getVol().getId() != null && rr.getVol().getId().equals(vi.getId()) && rr.getPrix() != null) {
                            boolean hasPaidOrEmis = billetRepository.findAll().stream().anyMatch(b ->
                                    b.getReservation() != null && b.getReservation().getId() != null && b.getReservation().getId().equals(rr.getId())
                                            && b.getStatusBillet() != null && b.getStatusBillet().getStatus() != null
                                            && (b.getStatusBillet().getStatus().toLowerCase().contains("payer") || b.getStatusBillet().getStatus().toLowerCase().contains("emis"))
                            );
                            if (hasPaidOrEmis) ticketsSum = ticketsSum.add(rr.getPrix());
                        }
                    }
                } catch (Exception ex) { /* ignore */ }

                // publicity revenue for this instance
                try {
                    java.util.List<com.itu.aviation.entity.Publicite> pubList = publiciteRepository.findByVolInstance(vi);
                    for (com.itu.aviation.entity.Publicite p : pubList) {
                        if (p.getMontant() != null) adsSum = adsSum.add(new java.math.BigDecimal(p.getMontant()));
                    }
                } catch (Exception ex) { /* ignore */ }
            }

            java.math.BigDecimal total = ticketsSum.add(adsSum);
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("vol", v);
            m.put("ticketsSum", ticketsSum);
            m.put("adsSum", adsSum);
            m.put("total", total);
            volCaList.add(m);
        }

        mav.addObject("vols", vols);
        mav.addObject("volCaList", volCaList);
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

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable Long id, @RequestParam(required = false) String date) {
        ModelAndView mav = new ModelAndView("vols/details");
        mav.addObject("pageTitle", "Détails Vol");
        mav.addObject("currentPage", "vols");
        com.itu.aviation.entity.Vol vol = volService.findById(id);
        mav.addObject("vol", vol);

        // parse optional date filter for instances
        java.time.LocalDate filterDate = null;
        if (date != null && !date.isBlank()) {
            try { filterDate = java.time.LocalDate.parse(date); } catch (Exception ex) { filterDate = null; }
            mav.addObject("filterDate", date);
        }
        final java.time.LocalDate filterDateRef = filterDate;

        // build vol instances view with CA per instance
        java.util.List<java.util.Map<String, Object>> volInstancesView = volInstanceService.findAll().stream()
            .filter(vi -> vi.getVol() != null && vi.getVol().getId() != null && vi.getVol().getId().equals(id))
            .filter(vi -> {
                if (filterDateRef == null) return true;
                if (vi.getDateDepart() == null) return false;
                return vi.getDateDepart().toLocalDate().equals(filterDateRef);
            })
            .sorted((a, b) -> {
                if (a.getDateDepart() == null && b.getDateDepart() == null) return 0;
                if (a.getDateDepart() == null) return -1;
                if (b.getDateDepart() == null) return 1;
                return a.getDateDepart().compareTo(b.getDateDepart());
            })
            .map(vi -> {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("id", vi.getId());
                m.put("vol", vi.getVol());
                m.put("avion", vi.getAvion());
                m.put("dateDepart", vi.getDateDepart());
                m.put("dateArrivee", vi.getDateArrivee());
                m.put("dateDepartDate", toDate(vi.getDateDepart()));
                m.put("dateArriveeDate", toDate(vi.getDateArrivee()));
                m.put("dateDepartYear", vi.getDateDepart() != null ? vi.getDateDepart().getYear() : null);
                m.put("dateDepartMonth", vi.getDateDepart() != null ? vi.getDateDepart().getMonthValue() : null);

                // tickets CA
                java.math.BigDecimal tickets = java.math.BigDecimal.ZERO;
                try {
                    java.util.List<com.itu.aviation.entity.Reservation> allRes = reservationService.findAll();
                    for (com.itu.aviation.entity.Reservation rr : allRes) {
                        if (rr.getVol() != null && rr.getVol().getId() != null && rr.getVol().getId().equals(vi.getId()) && rr.getPrix() != null) {
                            boolean hasPaidOrEmis = billetRepository.findAll().stream().anyMatch(b ->
                                    b.getReservation() != null && b.getReservation().getId() != null && b.getReservation().getId().equals(rr.getId())
                                            && b.getStatusBillet() != null && b.getStatusBillet().getStatus() != null
                                            && (b.getStatusBillet().getStatus().toLowerCase().contains("payer") || b.getStatusBillet().getStatus().toLowerCase().contains("emis"))
                            );
                            if (hasPaidOrEmis) tickets = tickets.add(rr.getPrix());
                        }
                    }
                } catch (Exception ex) { /* ignore */ }
                m.put("tickets", tickets);

                // publicity CA
                java.math.BigDecimal ads = java.math.BigDecimal.ZERO;
                try {
                    java.util.List<com.itu.aviation.entity.Publicite> pubList = publiciteRepository.findByVolInstance(vi);
                    for (com.itu.aviation.entity.Publicite p : pubList) {
                        if (p.getMontant() != null) ads = ads.add(new java.math.BigDecimal(p.getMontant()));
                    }
                } catch (Exception ex) { /* ignore */ }
                m.put("ads", ads);

                m.put("total", tickets.add(ads));
                return m;
            }).toList();

        // compute totals for this vol
        java.math.BigDecimal totalTickets = java.math.BigDecimal.ZERO;
        java.math.BigDecimal totalAds = java.math.BigDecimal.ZERO;
        for (java.util.Map<String, Object> mv : volInstancesView) {
            Object t = mv.get("tickets"); if (t instanceof java.math.BigDecimal) totalTickets = totalTickets.add((java.math.BigDecimal) t);
            Object a = mv.get("ads"); if (a instanceof java.math.BigDecimal) totalAds = totalAds.add((java.math.BigDecimal) a);
        }
        mav.addObject("volInstances", volInstancesView);
        mav.addObject("totalTickets", totalTickets);
        mav.addObject("totalAds", totalAds);
        mav.addObject("totalCA", totalTickets.add(totalAds));

        return mav;
    }

    private java.util.Date toDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return java.util.Date.from(dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
    }
}
