package com.itu.aviation.controller;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.entity.Reservation;
import com.itu.aviation.entity.Vol;
import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.entity.Siege;
import com.itu.aviation.entity.SiegeVol;
import com.itu.aviation.entity.StatusBillet;
import com.itu.aviation.entity.StatutReservation;
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
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private com.itu.aviation.service.CategoriePassagerService categoriePassagerService;

    @Autowired
    private AeroportRepository aeroportRepository;
    
    @Autowired
    private com.itu.aviation.repository.TypePassagerRepository typePassagerRepository;
    
    @Autowired
    private com.itu.aviation.repository.BilletRepository billetRepository;

    @Autowired
    private com.itu.aviation.repository.StatusBilletRepository statusBilletRepository;

    @Autowired
    private com.itu.aviation.service.ConfigService configService;

    @Autowired
    private com.itu.aviation.service.FidelitePassagerService fidelitePassagerService;

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

        java.util.List<Reservation> reservations;
        if (hasFilter) {
            java.time.LocalDate d = null;
            if (dateReservation != null)
                d = dateReservation;
            reservations = reservationService.search(numero, passager, vol, status, d);
        } else {
            reservations = reservationService.findAll();
        }
        mav.addObject("reservations", reservations);

        // compute revenue per VolInstance from the reservations being displayed
        java.util.Map<Long, BigDecimal> revenueMap = new java.util.HashMap<>();
        for (Reservation r : reservations) {
            if (r.getVol() != null && r.getVol().getId() != null && r.getPrix() != null) {
                Long vid = r.getVol().getId();
                revenueMap.put(vid, revenueMap.getOrDefault(vid, BigDecimal.ZERO).add(r.getPrix()));
            }
        }
        java.util.List<java.util.Map<String, Object>> revenueSummaries = new java.util.ArrayList<>();
        for (java.util.Map.Entry<Long, BigDecimal> e : revenueMap.entrySet()) {
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
            // debug: show count before filtering by date
            System.out.println("[DEBUG] volInstance count before date filter: " + vols.size());

            // Keep instances where the selected date falls within the instance interval
            vols = vols.stream()
                .filter(v -> v.getDateDepart() != null && v.getDateArrivee() != null
                    && (!date.isBefore(v.getDateDepart().toLocalDate()) && !date.isAfter(v.getDateArrivee().toLocalDate())))
                .toList();

            // debug: show count after filtering by date
            System.out.println("[DEBUG] volInstance count after date filter: " + vols.size());
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

        // Load categories and build price matrix (classId -> (categorieId -> prix)) for this vol
        java.util.List<com.itu.aviation.entity.CategoriePassager> categories = categoriePassagerService.findAll();
        mav.addObject("categories", categories);

        java.util.List<com.itu.aviation.entity.PrixVolClasse> allPrixForVol = prixVolClasseService.findAll().stream()
            .filter(p -> p.getVol() != null && p.getVol().getId().equals(selectedVol.getVol().getId()))
            .toList();

        java.util.Map<Long, java.util.Map<Long, BigDecimal>> priceMatrix = new java.util.HashMap<>();
        for (com.itu.aviation.entity.PrixVolClasse p : allPrixForVol) {
            if (p.getClasse() == null || p.getClasse().getId() == null) continue;
            Long classeId = p.getClasse().getId();
            Long catId = p.getCategoriePassager() != null ? p.getCategoriePassager().getId() : null;
            priceMatrix.computeIfAbsent(classeId, k -> new java.util.HashMap<>());
            if (catId != null) priceMatrix.get(classeId).put(catId, p.getPrix());
        }
        mav.addObject("priceMatrix", priceMatrix);

        // Determine default category (prefer 'Adulte')
        Long defaultCatId = null;
        for (com.itu.aviation.entity.CategoriePassager c : categories) {
            if (c.getCategorie() != null && "Adulte".equalsIgnoreCase(c.getCategorie())) {
                defaultCatId = c.getId();
                break;
            }
        }
        if (defaultCatId == null && !categories.isEmpty()) defaultCatId = categories.get(0).getId();

        // Prix par classe pour ce vol for default category
        Map<Long, BigDecimal> priceByClass = new java.util.HashMap<>();
        for (Map.Entry<Long, java.util.Map<Long, BigDecimal>> e : priceMatrix.entrySet()) {
            Long classeId = e.getKey();
            BigDecimal pr = e.getValue().getOrDefault(defaultCatId, BigDecimal.ZERO);
            priceByClass.put(classeId, pr);
        }

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

                // no server-side seat reservation blocking: selectable based solely on SiegeVol status

            dto.put("status", status);
            dto.put("selectable", selectable);
            dto.put("price", priceByClass.getOrDefault(s.getClasse() != null ? s.getClasse().getId() : -1L,
                    BigDecimal.ZERO));

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
            if(siegeIds !=null){
                System.out.println("[DEBUG] Received siegeIds: " + siegeIds);

                Optional<Reservation> reserv = reservationService.findReservationByVolInstanceAndSiege(reservation.getVol().getId(),
                        Long.parseLong(siegeIds.split(",")[0]));

                if(reserv.isPresent()){
                    if(reserv.get().getStatutReservation().equals("En attente")){

                        throw new IllegalStateException("Le siège sélectionné est en attente de reservation.");
                    }else if(reserv.get().getStatutReservation().equals("Confirmée")){
                        throw new IllegalStateException("Le siège sélectionné est déjà réservé.");
                    }
                }

        
            }
            if(reservation !=null){
                if(reservation.getVol()!=null && reservation.getVol().getId()!=null){
                    // fetch managed VolInstance
                    VolInstance vi = volInstanceService.findById(reservation.getVol().getId());
                    reservation.setVol(vi);

                    if(reservation.getDateReservation().isAfter(vi.getDateDepart())){
                        throw new IllegalStateException("La date de réservation ne peut pas être supérieure à la date de départ du vol.");
                    }


                }
            }
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

                // If categoriePassager id was bound from the form, replace with managed entity
                if (reservation.getPassager().getCategoriePassager() != null && reservation.getPassager().getCategoriePassager().getId() != null) {
                    Long cpId = reservation.getPassager().getCategoriePassager().getId();
                    try {
                        com.itu.aviation.entity.CategoriePassager cp = categoriePassagerService.findById(cpId);
                        reservation.getPassager().setCategoriePassager(cp);
                    } catch (Exception ex) {
                        // ignore if not found
                    }
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
            // ensure price computed if missing/zero
            if ((reservation.getPrix() == null || reservation.getPrix().compareTo(BigDecimal.ZERO) == 0)
                    && reservation.getVol() != null && reservation.getClasse() != null) {
                reservation.setPrix(computePriceLocal(reservation));
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
                // ensure price computed before update as well
                if ((reservation.getPrix() == null || reservation.getPrix().compareTo(BigDecimal.ZERO) == 0)
                        && reservation.getVol() != null && reservation.getClasse() != null) {
                    reservation.setPrix(computePriceLocal(reservation));
                }
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

            // ========== AJOUT DES POINTS DE FIDÉLITÉ ==========
            if (r.getPassager() != null) {
                // Récupérer les points depuis la configuration
                Integer pointsConfig = configService.getPointsFidelite();
                
                if (pointsConfig != null && pointsConfig > 0) {
                    com.itu.aviation.entity.Passager passager = r.getPassager();
                    
                    // Ajouter les points au total du passager
                    Integer currentPoints = passager.getFidelite() != null ? passager.getFidelite() : 0;
                    passager.setFidelite(currentPoints + pointsConfig);
                    passagerService.update(passager.getId(), passager);
                    
                    // Créer une entrée dans fidelite_passager pour l'historique
                    // Utiliser la date de réservation comme date d'obtention des points
                    java.time.LocalDateTime dateReservation = r.getDateReservation();
                    fidelitePassagerService.addPoints(passager, pointsConfig, dateReservation);
                }
            }
            // ==================================================

            // create billet if none exists
            boolean exists = billetRepository.findAll().stream().anyMatch(b -> b.getReservation() != null && b.getReservation().getId().equals(id));
                if (!exists) {
                Billet b = new Billet();
                b.setReservation(r);
                b.setSiege(r.getSiege());
                b.setDateEmission(java.time.LocalDateTime.now());
                String num = "BIL-" + (r.getId() != null ? r.getId() : "X") + "-" + System.currentTimeMillis();
                b.setNumeroBillet(num);
                    b.setPrixFinal(r.getPrix());
                List<StatusBillet> statuses = statusBilletRepository.findAll();
                if (!statuses.isEmpty()) b.setStatusBillet(statuses.get(4));
                billetRepository.save(b);
            }

            // Message avec les points gagnés
            Integer pointsGagnes = configService.getPointsFidelite();
            String message = "Réservation confirmée et billet créé";
            if (pointsGagnes != null && pointsGagnes > 0 && r.getPassager() != null) {
                message += " - " + pointsGagnes + " point(s) de fidélité ajouté(s) à " + r.getPassager().getNom() + " " + r.getPassager().getPrenom();
            }
            redirect.addFlashAttribute("successMessage", message);
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Impossible de confirmer la réservation: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/recalculate-prices")
    public String recalculatePrices(RedirectAttributes redirect) {
        try {
            java.util.List<Reservation> all = reservationService.findAll();
            int updated = 0;
            for (Reservation r : all) {
                if (r.getVol() != null && r.getClasse() != null) {
                    BigDecimal newPrice = computePriceLocal(r);
                    if (r.getPrix() == null || r.getPrix().compareTo(newPrice) != 0) {
                        r.setPrix(newPrice);
                        reservationService.update(r.getId(), r);
                        updated++;
                    }
                }
            }
            redirect.addFlashAttribute("successMessage", "Recalculated prices for " + updated + " reservations");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors du recalcul des prix: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }

    // Local copy of pricing logic (keeps behaviour in controller without exposing service internals)
    private BigDecimal computePriceLocal(Reservation r) {
        final Long desiredCatId = (r.getPassager() != null && r.getPassager().getCategoriePassager() != null)
            ? r.getPassager().getCategoriePassager().getId() : null;

        Optional<com.itu.aviation.entity.PrixVolClasse> p = prixVolClasseService.findAll().stream()
            .filter(x -> x.getVol() != null && r.getVol()!=null && r.getVol().getVol()!=null && x.getVol().getId().equals(r.getVol().getVol().getId())
                && x.getClasse() != null && r.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId())
                && ((desiredCatId == null && x.getCategoriePassager() == null) || (x.getCategoriePassager() != null && desiredCatId != null && x.getCategoriePassager().getId().equals(desiredCatId))))
            .findFirst();

        BigDecimal base = BigDecimal.ZERO;
        if (p.isPresent()) {
            base = p.get().getPrix();
        } else {
            // fallback: any price for this vol+class (ignore category)
            Optional<com.itu.aviation.entity.PrixVolClasse> any = prixVolClasseService.findAll().stream()
                .filter(x -> x.getVol() != null && r.getVol()!=null && r.getVol().getVol()!=null && x.getVol().getId().equals(r.getVol().getVol().getId())
                    && x.getClasse() != null && r.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId()))
                .findFirst();
            base = any.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(BigDecimal.ZERO);
        }

        // Return base price directly without dynamic adjustments
        // If selected category has a percentage reduction, apply it relative to Adulte base price
        if (desiredCatId != null) {
            try {
                com.itu.aviation.entity.CategoriePassager desiredCat = categoriePassagerService.findById(desiredCatId);
                java.math.BigDecimal pct = desiredCat.getPourcentageReduction();
                if (pct != null) {
                    Optional<com.itu.aviation.entity.PrixVolClasse> adultePriceOpt = prixVolClasseService.findAll().stream()
                        .filter(x -> x.getVol() != null && r.getVol()!=null && r.getVol().getVol()!=null && x.getVol().getId().equals(r.getVol().getVol().getId())
                            && x.getClasse() != null && r.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId())
                            && x.getCategoriePassager() != null && "Adulte".equalsIgnoreCase(x.getCategoriePassager().getCategorie()))
                        .findFirst();

                    java.math.BigDecimal adulteBase = adultePriceOpt.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(base);
                    if (adulteBase == null) adulteBase = java.math.BigDecimal.ZERO;
                    java.math.BigDecimal computed = adulteBase.multiply(pct).divide(new java.math.BigDecimal(100), 2, java.math.RoundingMode.HALF_UP);
                    return computed.setScale(2, RoundingMode.HALF_UP);
                }
            } catch (Exception ex) {
                // ignore and fallback to base
            }
        }

        return base.setScale(2, RoundingMode.HALF_UP);
    }
}
