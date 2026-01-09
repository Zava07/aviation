package com.itu.aviation.controller;

import com.itu.aviation.entity.Reservation;
import com.itu.aviation.entity.Vol;
import com.itu.aviation.entity.Siege;
import com.itu.aviation.entity.SiegeVol;

import com.itu.aviation.service.ReservationService;
import com.itu.aviation.service.PassagerService;
import com.itu.aviation.service.VolService;
import com.itu.aviation.service.ClasseService;
import com.itu.aviation.service.SiegeService;
import com.itu.aviation.service.PrixVolClasseService;
import com.itu.aviation.repository.AeroportRepository;
import com.itu.aviation.repository.StatutReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private VolService volService;

    @Autowired
    private com.itu.aviation.service.VolInstanceService volInstanceService;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private StatutReservationRepository statutReservationRepository;

    @Autowired
    private com.itu.aviation.service.SiegeVolService siegeVolService;

    @Autowired
    private SiegeService siegeService;

    @Autowired
    private PrixVolClasseService prixVolClasseService;

    @Autowired
    private AeroportRepository aeroportRepository;
    
    @Autowired
    private com.itu.aviation.repository.TypePassagerRepository typePassagerRepository;
    
    @Autowired
    private com.itu.aviation.repository.BilletRepository billetRepository;

    @Autowired
    private com.itu.aviation.repository.StatusBilletRepository statusBilletRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String numero,
            @RequestParam(required = false) String passager,
            @RequestParam(required = false) String vol,
            @RequestParam(required = false) Long status,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateReservation) {
        ModelAndView mav = new ModelAndView("reservations/list");
        mav.addObject("pageTitle", "Réservations");
        mav.addObject("currentPage", "reservations");

        // perform server-side search if any filter is provided
        boolean hasFilter = (numero != null && !numero.isBlank()) || (passager != null && !passager.isBlank()) ||
                (vol != null && !vol.isBlank()) || (status != null) || (dateReservation != null);

        if (hasFilter) {
            java.time.LocalDate d = null;
            if (dateReservation != null)
                d = dateReservation;
            mav.addObject("reservations", reservationService.search(numero, passager, vol, status, d));
        } else {
            mav.addObject("reservations", reservationService.findAll());
        }

        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("vols", volService.findAll());
        mav.addObject("classes", classeService.findAll());
        mav.addObject("statusList", statutReservationRepository.findAll());
        mav.addObject("sieges", siegeVolService.findAll());

        // Add filter values back to model so form keeps them
        mav.addObject("filterNumero", numero);
        mav.addObject("filterPassager", passager);
        mav.addObject("filterVol", vol);
        mav.addObject("filterStatus", status);
        mav.addObject("filterDate", dateReservation);

        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("reservations/list");
        mav.addObject("pageTitle", "Réservations");
        mav.addObject("currentPage", "reservations");
        mav.addObject("reservations", reservationService.findAll());
        mav.addObject("passagers", passagerService.findAll());
        mav.addObject("vols", volService.findAll());
        mav.addObject("classes", classeService.findAll());
        mav.addObject("statusList", statutReservationRepository.findAll());
        mav.addObject("sieges", siegeVolService.findAll());
        mav.addObject("editReservation", reservationService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create(
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date,
            @RequestParam(required = false) Long departId,
            @RequestParam(required = false) Long destId,
            @RequestParam(required = false) Long volId) {

        ModelAndView mav = new ModelAndView("reservations/form");
        mav.addObject("pageTitle", "Nouvelle Réservation");
        mav.addObject("currentPage", "reservations");

        mav.addObject("aeroports", aeroportRepository.findAll());
        mav.addObject("types", typePassagerRepository.findAll());
        mav.addObject("classes", classeService.findAll());
        mav.addObject("statusList", statutReservationRepository.findAll());

        // Filtrer les instances de vols (vol_instance) par date et aéroports
        List<com.itu.aviation.entity.VolInstance> vols = volInstanceService.findAll();
        if (date != null) {
            vols = vols.stream()
                .filter(v -> v.getDateDepart() != null && v.getDateDepart().toLocalDate().equals(date))
                .toList();
        }
        if (departId != null) {
            vols = vols.stream()
                .filter(v -> v.getVol() != null && v.getVol().getAeroportOrigine() != null && v.getVol().getAeroportOrigine().getId().equals(departId))
                .toList();
        }
        if (destId != null) {
            vols = vols.stream()
                .filter(v -> v.getVol() != null && v.getVol().getAeroportDestination() != null
                    && v.getVol().getAeroportDestination().getId().equals(destId))
                .toList();
        }

        mav.addObject("vols", vols);

        if (vols.isEmpty()) {
            mav.addObject("noFlights", true);
            return mav;
        }

        // Définir le vol sélectionné (final pour utilisation dans les lambdas)
        final com.itu.aviation.entity.VolInstance selectedVol;
        if (volId != null) {
            selectedVol = vols.stream()
                .filter(v -> v.getId().equals(volId))
                .findFirst()
                .orElse(vols.get(0));
        } else {
            selectedVol = vols.get(0);
        }
        mav.addObject("selectedVol", selectedVol);

        // Construire le plan des sièges pour selectedVol (instance -> avion)
        List<Siege> allSieges = siegeService.findAll().stream()
            .filter(s -> s.getAvion() != null && s.getAvion().getId().equals(selectedVol.getAvion().getId()))
            .toList();

        Map<Long, SiegeVol> siegeVolMap = siegeVolService.findAll().stream()
            .filter(sv -> sv.getVolInstance() != null && sv.getVolInstance().getId().equals(selectedVol.getId()))
            .collect(java.util.stream.Collectors.toMap(sv -> sv.getSiege().getId(), sv -> sv));

        // Prix par classe pour ce vol (prix are at Vol/route level)
        Map<Long, BigDecimal> priceByClass = prixVolClasseService.findAll().stream()
            .filter(p -> p.getVol() != null && p.getVol().getId().equals(selectedVol.getVol().getId()))
            .collect(java.util.stream.Collectors.toMap(p -> p.getClasse().getId(), p -> p.getPrix()));

        // Préparer les DTOs des sièges
        List<Map<String, Object>> seats = new java.util.ArrayList<>();
        for (Siege s : allSieges) {
            Map<String, Object> dto = new java.util.HashMap<>();
            dto.put("id", s.getId());
            dto.put("numero", s.getNumero());
            dto.put("classeId", s.getClasse() != null ? s.getClasse().getId() : null);
            dto.put("classeNom", s.getClasse() != null ? s.getClasse().getNomClasse() : "");

            SiegeVol sv = siegeVolMap.get(s.getId());
            String status = "Libre";
            if (sv != null && sv.getStatusSiegeVol() != null && sv.getStatusSiegeVol().getStatus() != null) {
                status = sv.getStatusSiegeVol().getStatus();
            }
            boolean selectable = "Libre".equalsIgnoreCase(status) || "Free".equalsIgnoreCase(status);

            dto.put("status", status);
            dto.put("selectable", selectable);
            dto.put("price", priceByClass.getOrDefault(s.getClasse() != null ? s.getClasse().getId() : -1L,
                    java.math.BigDecimal.ZERO));

            seats.add(dto);
        }

        mav.addObject("seats", seats);
        mav.addObject("prices", priceByClass);

        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Reservation reservation, @RequestParam(required = false) String siegeIds,
            RedirectAttributes redirect) {
        try {
            // Ensure passenger is persisted: if passager provided without id, try to find existing by nom+prenom+email
            if (reservation.getPassager() != null && reservation.getPassager().getId() == null) {
                String nom = reservation.getPassager().getNom();
                String prenom = reservation.getPassager().getPrenom();
                String email = reservation.getPassager().getEmail();

                // If typePassager id was bound from the form, replace with managed entity
                if (reservation.getPassager().getTypePassager() != null && reservation.getPassager().getTypePassager().getId() != null) {
                    Long tpId = reservation.getPassager().getTypePassager().getId();
                    typePassagerRepository.findById(tpId).ifPresent(tp -> reservation.getPassager().setTypePassager(tp));
                }

                if (nom != null || prenom != null || email != null) {
                    java.util.Optional<com.itu.aviation.entity.Passager> existing = passagerService.findByNomPrenomEmail(
                            nom == null ? "" : nom.trim(), prenom == null ? "" : prenom.trim(),
                            email == null ? "" : email.trim());
                    if (existing.isPresent()) {
                        reservation.setPassager(existing.get());
                    } else {
                        // create new passenger (typePassager now managed if provided)
                        com.itu.aviation.entity.Passager saved = passagerService.create(reservation.getPassager());
                        reservation.setPassager(saved);
                    }
                }
            }
            // If multiple seat ids came from the form, attach the first one to the
            // reservation for backward compatibility.
            if (siegeIds != null && !siegeIds.isBlank()) {
                String[] parts = siegeIds.split(",");
                try {
                    Long firstId = Long.parseLong(parts[0]);
                    reservation.setSiege(siegeService.findById(firstId));
                } catch (Exception e) {
                    // ignore parse errors; proceed without siege - service validations will catch
                    // it
                }
            }
            if (reservation.getId() == null) {
                Reservation saved = reservationService.create(reservation);
                redirect.addFlashAttribute("successMessage", "Réservation créée");
                // if statut contains 'confirm', generate billet
                if (saved.getStatutReservation() != null && saved.getStatutReservation().getNomStatus() != null
                        && saved.getStatutReservation().getNomStatus().toLowerCase().contains("confirm")) {
                    com.itu.aviation.entity.Billet b = new com.itu.aviation.entity.Billet();
                    b.setReservation(saved);
                    b.setSiege(saved.getSiege());
                    b.setDateEmission(java.time.LocalDateTime.now());
                    String num = "BIL-" + (saved.getId() != null ? saved.getId() : "X") + "-" + System.currentTimeMillis();
                    b.setNumeroBillet(num);
                    b.setPrixFinal(reservation.getPrix());
                    b.setPrixFinal(saved.getPrix());
                    java.util.List<com.itu.aviation.entity.StatusBillet> statuses = statusBilletRepository.findAll();
                    if (!statuses.isEmpty()) b.setStatusBillet(statuses.get(0));
                    billetRepository.save(b);
                }
            } else {
                Reservation saved = reservationService.update(reservation.getId(), reservation);
                redirect.addFlashAttribute("successMessage", "Réservation mise à jour");
                if (saved.getStatutReservation() != null && saved.getStatutReservation().getNomStatus() != null
                        && saved.getStatutReservation().getNomStatus().toLowerCase().contains("confirm")) {
                    // create billet if none exists for this reservation
                    boolean exists = billetRepository.findAll().stream().anyMatch(b -> b.getReservation() != null && b.getReservation().getId().equals(saved.getId()));
                    if (!exists) {
                        com.itu.aviation.entity.Billet b = new com.itu.aviation.entity.Billet();
                        b.setReservation(saved);
                        b.setSiege(saved.getSiege());
                        b.setDateEmission(java.time.LocalDateTime.now());
                        String num = "BIL-" + (saved.getId() != null ? saved.getId() : "X") + "-" + System.currentTimeMillis();
                        b.setNumeroBillet(num);
                        b.setPrixFinal(saved.getPrix());
                        java.util.List<com.itu.aviation.entity.StatusBillet> statuses = statusBilletRepository.findAll();
                        if (!statuses.isEmpty()) b.setStatusBillet(statuses.get(0));
                        billetRepository.save(b);
                    }
                }
            }
        } catch (IllegalStateException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage",
                    "Erreur lors de la création de la réservation: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reservationService.cancelReservation(id);
            redirect.addFlashAttribute("successMessage", "Réservation annulée");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Impossible d'annuler la réservation: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/confirm/{id}")
    public String confirmReservation(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Reservation r = reservationService.findById(id);

            // find a 'confirmed' statut
            java.util.Optional<com.itu.aviation.entity.StatutReservation> confirmOpt = statutReservationRepository.findByNomStatusContainingIgnoreCase("confirm");
            com.itu.aviation.entity.StatutReservation confirmStatut = confirmOpt.orElseGet(() -> statutReservationRepository.findAll().stream().findFirst().orElse(null));
            if (confirmStatut == null) throw new IllegalStateException("Aucun statut de réservation disponible");

            r.setStatutReservation(confirmStatut);
            reservationService.update(id, r);

            // create billet if none exists
            boolean exists = billetRepository.findAll().stream().anyMatch(b -> b.getReservation() != null && b.getReservation().getId().equals(id));
            if (!exists) {
                com.itu.aviation.entity.Billet b = new com.itu.aviation.entity.Billet();
                b.setReservation(r);
                b.setSiege(r.getSiege());
                b.setDateEmission(java.time.LocalDateTime.now());
                String num = "BIL-" + (r.getId() != null ? r.getId() : "X") + "-" + System.currentTimeMillis();
                b.setNumeroBillet(num);
                java.util.List<com.itu.aviation.entity.StatusBillet> statuses = statusBilletRepository.findAll();
                if (!statuses.isEmpty()) b.setStatusBillet(statuses.get(0));
                billetRepository.save(b);
            }

            redirect.addFlashAttribute("successMessage", "Réservation confirmée et billet créé");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Impossible de confirmer la réservation: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }
}
