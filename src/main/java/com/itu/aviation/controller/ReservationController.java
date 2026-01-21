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

    @Autowired
    private com.itu.aviation.service.PopulariteVolService populariteVolService;

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

            // ========== AJOUT DES POINTS DE POPULARITÉ AU VOL ==========
            if (r.getVol() != null && r.getVol().getVol() != null) {
                Integer pointsVolConfig = configService.getPointVol();
                if (pointsVolConfig != null && pointsVolConfig > 0) {
                    Vol vol = r.getVol().getVol();
                    
                    // Ajouter les points au total du vol
                    Integer currentVolPoints = vol.getPointVol() != null ? vol.getPointVol() : 0;
                    vol.setPointVol(currentVolPoints + pointsVolConfig);
                    volService.update(vol.getId(), vol);
                    
                    // Créer une entrée dans popularite_vol pour l'historique
                    populariteVolService.addPoints(vol, pointsVolConfig, r.getDateReservation());
                }
            }
            // ===========================================================

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
            Integer pointsVolGagnes = configService.getPointVol();
            String message = "Réservation confirmée et billet créé";
            if (pointsGagnes != null && pointsGagnes > 0 && r.getPassager() != null) {
                message += " - " + pointsGagnes + " point(s) de fidélité ajouté(s) à " + r.getPassager().getNom() + " " + r.getPassager().getPrenom();
            }
            if (pointsVolGagnes != null && pointsVolGagnes > 0 && r.getVol() != null && r.getVol().getVol() != null) {
                message += " - " + pointsVolGagnes + " point(s) de popularité ajouté(s) au vol " + r.getVol().getVol().getNumeroVol();
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

    // ========== SAVE MULTIPLE RESERVATIONS WITH AUTO SEAT ASSIGNMENT ==========
    @PostMapping("/save-multiple")
    public String saveMultiple(@RequestParam Long volInstanceId,
                               @RequestParam String passagerNom,
                               @RequestParam String passagerPrenom,
                               @RequestParam String passagerEmail,
                               @RequestParam(required = false) Long statutId,
                               @RequestParam(name = "dateReservation", required = false) String dateReservation,
                               @RequestParam(name = "linesClassId") java.util.List<String> linesClassId,
                               @RequestParam(name = "linesCategorieId", required = false) java.util.List<String> linesCategorieId,
                               @RequestParam(name = "linesQty") java.util.List<String> linesQty,
                               @RequestParam(name = "linesPrice", required = false) java.util.List<String> linesPrice,
                               RedirectAttributes redirect) {
        try {
            // 1) Ensure or create passager
            com.itu.aviation.entity.Passager passager = null;
            java.util.Optional<com.itu.aviation.entity.Passager> existing = passagerService.findByNomPrenomEmail(
                    passagerNom == null ? "" : passagerNom.trim(),
                    passagerPrenom == null ? "" : passagerPrenom.trim(),
                    passagerEmail == null ? "" : passagerEmail.trim());
            if (existing.isPresent()) {
                passager = existing.get();
            } else {
                com.itu.aviation.entity.Passager p = new com.itu.aviation.entity.Passager();
                p.setNom(passagerNom);
                p.setPrenom(passagerPrenom);
                p.setEmail(passagerEmail);
                // ensure required TypePassager is set (NOT NULL constraint)
                java.util.List<com.itu.aviation.entity.TypePassager> tps = typePassagerRepository.findAll();
                if (!tps.isEmpty()) p.setTypePassager(tps.get(0));
                passager = passagerService.create(p);
            }

            com.itu.aviation.entity.VolInstance vi = volInstanceService.findById(volInstanceId);

            // 2) Determine statut to apply
            com.itu.aviation.entity.StatutReservation statut = null;
            if (statutId != null) {
                try { statut = statutReservationRepository.findById(statutId).orElse(null); } catch (Exception ex) { statut = null; }
            }
            if (statut == null) {
                java.util.Optional<com.itu.aviation.entity.StatutReservation> opt = statutReservationRepository.findByNomStatusContainingIgnoreCase("en attente");
                statut = opt.orElseGet(() -> statutReservationRepository.findAll().stream().findFirst().orElse(null));
            }

            // 3) Parse optional reservation date
            java.time.LocalDateTime reservationDate = null;
            if (dateReservation != null && !dateReservation.trim().isEmpty()) {
                try { reservationDate = java.time.LocalDateTime.parse(dateReservation); } catch (Exception ex) { /* ignore */ }
            }

            // 4) Get all seats for this vol instance's avion, grouped by class
            java.util.List<Siege> allSieges = siegeService.findAll().stream()
                .filter(s -> s.getAvion() != null && vi.getAvion() != null && s.getAvion().getId().equals(vi.getAvion().getId()))
                .toList();

            // Get SiegeVol status map for this vol instance
            java.util.Map<Long, SiegeVol> siegeVolMap = siegeVolService.findAll().stream()
                .filter(sv -> sv.getVolInstance() != null && sv.getVolInstance().getId().equals(vi.getId()))
                .collect(java.util.stream.Collectors.toMap(sv -> sv.getSiege().getId(), sv -> sv, (a, b) -> a));

            // Get already reserved siege IDs for this vol instance
            java.util.Set<Long> reservedSiegeIds = reservationService.findAll().stream()
                .filter(r -> r.getVol() != null && r.getVol().getId().equals(vi.getId()) && r.getSiege() != null)
                .map(r -> r.getSiege().getId())
                .collect(java.util.stream.Collectors.toSet());

            // Build map: classeId -> list of free siege IDs
            java.util.Map<Long, java.util.List<Long>> freeSeatsByClass = new java.util.HashMap<>();
            for (Siege s : allSieges) {
                Long classeId = s.getClasse() != null ? s.getClasse().getId() : null;
                if (classeId == null) continue;

                // Check SiegeVol status
                SiegeVol sv = siegeVolMap.get(s.getId());
                String status = "Libre";
                if (sv != null && sv.getStatusSiegeVol() != null && sv.getStatusSiegeVol().getStatus() != null) {
                    status = sv.getStatusSiegeVol().getStatus();
                }
                boolean isFree = "Libre".equalsIgnoreCase(status) || "Free".equalsIgnoreCase(status);

                // Also check not already reserved
                if (isFree && !reservedSiegeIds.contains(s.getId())) {
                    freeSeatsByClass.computeIfAbsent(classeId, k -> new java.util.ArrayList<>()).add(s.getId());
                }
            }

            // 5) Process each line and create reservations
            int totalCreated = 0;
            
            // DEBUG: Log received data
            System.out.println("[DEBUG save-multiple] linesClassId: " + linesClassId);
            System.out.println("[DEBUG save-multiple] linesCategorieId: " + linesCategorieId);
            System.out.println("[DEBUG save-multiple] linesQty: " + linesQty);
            System.out.println("[DEBUG save-multiple] linesPrice: " + linesPrice);
            
            for (int i = 0; i < linesClassId.size(); i++) {
                String cls = linesClassId.get(i);
                if (cls == null || cls.trim().isEmpty()) continue;
                Long classId = Long.parseLong(cls);

                Long catId = null;
                if (linesCategorieId != null && linesCategorieId.size() > i) {
                    String c = linesCategorieId.get(i);
                    if (c != null && !c.trim().isEmpty()) catId = Long.parseLong(c);
                }

                int qty = 1;
                if (linesQty != null && linesQty.size() > i) {
                    try { qty = Integer.parseInt(linesQty.get(i)); } catch (Exception ex) { qty = 1; }
                }

                // Get free seats for this class
                java.util.List<Long> freeSeats = freeSeatsByClass.getOrDefault(classId, new java.util.ArrayList<>());
                if (freeSeats.size() < qty) {
                    com.itu.aviation.entity.Classe cl = classeService.findById(classId);
                    String className = cl != null ? cl.getNomClasse() : ("ID " + classId);
                    throw new IllegalStateException("Pas assez de sièges disponibles pour la classe " + className
                            + ". Demandé: " + qty + ", Disponible: " + freeSeats.size());
                }

                // Compute price for this class+category
                final Long desiredCatId = catId;
                
                // Get all prices for this vol and class
                java.util.List<com.itu.aviation.entity.PrixVolClasse> pricesForVolClass = prixVolClasseService.findAll().stream()
                        .filter(x -> x.getVol() != null && vi.getVol() != null && x.getVol().getId().equals(vi.getVol().getId())
                                && x.getClasse() != null && x.getClasse().getId().equals(classId))
                        .toList();

                java.math.BigDecimal price = java.math.BigDecimal.ZERO;
                boolean foundExactCategoryPrice = false;

                // 1) First, try to find exact category price
                if (desiredCatId != null) {
                    for (com.itu.aviation.entity.PrixVolClasse pvc : pricesForVolClass) {
                        if (pvc.getCategoriePassager() != null && pvc.getCategoriePassager().getId().equals(desiredCatId)) {
                            price = pvc.getPrix();
                            foundExactCategoryPrice = true;
                            break;
                        }
                    }
                }

                // 2) If no exact category price found, check if category has percentage reduction
                if (!foundExactCategoryPrice && desiredCatId != null) {
                    try {
                        com.itu.aviation.entity.CategoriePassager desiredCat = categoriePassagerService.findById(desiredCatId);
                        java.math.BigDecimal pct = desiredCat.getPourcentageReduction();
                        if (pct != null) {
                            // Find Adulte price for this class
                            java.math.BigDecimal adultePrice = null;
                            for (com.itu.aviation.entity.PrixVolClasse pvc : pricesForVolClass) {
                                if (pvc.getCategoriePassager() != null && "Adulte".equalsIgnoreCase(pvc.getCategoriePassager().getCategorie())) {
                                    adultePrice = pvc.getPrix();
                                    break;
                                }
                            }
                            if (adultePrice != null) {
                                price = adultePrice.multiply(pct).divide(new java.math.BigDecimal(100), 2, java.math.RoundingMode.HALF_UP);
                                foundExactCategoryPrice = true; // Mark as found to avoid fallback
                            }
                        }
                    } catch (Exception ex) { /* ignore */ }
                }

                // 3) Ultimate fallback: use first available price for this class (should rarely happen)
                if (!foundExactCategoryPrice && !pricesForVolClass.isEmpty()) {
                    price = pricesForVolClass.get(0).getPrix();
                }

                // 4) PREFER client-submitted price (like single reservation does)
                System.out.println("[DEBUG save-multiple] Line " + i + ": classId=" + classId + ", catId=" + catId + ", qty=" + qty);
                System.out.println("[DEBUG save-multiple] Line " + i + ": server computed price=" + price);
                
                if (linesPrice != null && linesPrice.size() > i) {
                    String clientPriceStr = linesPrice.get(i);
                    System.out.println("[DEBUG save-multiple] Line " + i + ": clientPriceStr=" + clientPriceStr);
                    if (clientPriceStr != null && !clientPriceStr.trim().isEmpty()) {
                        try {
                            java.math.BigDecimal clientPrice = new java.math.BigDecimal(clientPriceStr.trim());
                            System.out.println("[DEBUG save-multiple] Line " + i + ": parsed clientPrice=" + clientPrice);
                            if (clientPrice.compareTo(java.math.BigDecimal.ZERO) > 0) {
                                price = clientPrice; // Use client-calculated price
                                System.out.println("[DEBUG save-multiple] Line " + i + ": USING CLIENT PRICE=" + price);
                            }
                        } catch (Exception ex) { 
                            System.out.println("[DEBUG save-multiple] Line " + i + ": parse error: " + ex.getMessage());
                        }
                    }
                } else {
                    System.out.println("[DEBUG save-multiple] Line " + i + ": linesPrice is null or too short");
                }
                
                System.out.println("[DEBUG save-multiple] Line " + i + ": FINAL price=" + price);

                // Create qty reservations with auto-assigned seats
                for (int k = 0; k < qty; k++) {
                    Long siegeId = freeSeats.remove(0); // Take and remove from available list
                    Siege siege = siegeService.findById(siegeId);

                    Reservation r = new Reservation();
                    r.setPassager(passager);
                    r.setVol(vi);
                    r.setClasse(classeService.findById(classId));
                    r.setSiege(siege);
                    r.setStatutReservation(statut);
                    if (reservationDate != null) r.setDateReservation(reservationDate);
                    r.setPrix(price.setScale(2, java.math.RoundingMode.HALF_UP));

                    reservationService.create(r);
                    totalCreated++;

                    // Mark seat as reserved in global tracking
                    reservedSiegeIds.add(siegeId);
                }
            }

            redirect.addFlashAttribute("successMessage", totalCreated + " réservation(s) créée(s) avec attribution automatique des sièges.");
        } catch (IllegalStateException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la création des réservations multiples: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }

    // ========== CONFIRM AND PAY ALL PENDING RESERVATIONS ==========
    @PostMapping("/confirm-all")
    public String confirmAll(RedirectAttributes redirect) {
        try {
            java.util.List<Reservation> allReservations = reservationService.findAll();
            int confirmedCount = 0;
            int totalPoints = 0;
            int totalVolPoints = 0;

            // Find 'confirmed' status
            java.util.Optional<com.itu.aviation.entity.StatutReservation> confirmOpt = statutReservationRepository.findByNomStatusContainingIgnoreCase("confirm");
            com.itu.aviation.entity.StatutReservation confirmStatut = confirmOpt.orElseGet(() -> statutReservationRepository.findAll().stream().findFirst().orElse(null));
            if (confirmStatut == null) throw new IllegalStateException("Aucun statut de confirmation disponible");

            for (Reservation r : allReservations) {
                // Skip already confirmed reservations
                if (r.getStatutReservation() != null && r.getStatutReservation().getNomStatus() != null
                        && r.getStatutReservation().getNomStatus().toLowerCase().contains("confirm")) {
                    continue;
                }

                // Confirm this reservation
                r.setStatutReservation(confirmStatut);
                reservationService.update(r.getId(), r);
                confirmedCount++;

                // Add fidelity points
                if (r.getPassager() != null) {
                    Integer pointsConfig = configService.getPointsFidelite();
                    if (pointsConfig != null && pointsConfig > 0) {
                        com.itu.aviation.entity.Passager passager = r.getPassager();
                        Integer currentPoints = passager.getFidelite() != null ? passager.getFidelite() : 0;
                        passager.setFidelite(currentPoints + pointsConfig);
                        passagerService.update(passager.getId(), passager);
                        fidelitePassagerService.addPoints(passager, pointsConfig, r.getDateReservation());
                        totalPoints += pointsConfig;
                    }
                }

                // Add vol popularity points
                if (r.getVol() != null && r.getVol().getVol() != null) {
                    Integer pointsVolConfig = configService.getPointVol();
                    if (pointsVolConfig != null && pointsVolConfig > 0) {
                        Vol vol = r.getVol().getVol();
                        Integer currentVolPoints = vol.getPointVol() != null ? vol.getPointVol() : 0;
                        vol.setPointVol(currentVolPoints + pointsVolConfig);
                        volService.update(vol.getId(), vol);
                        populariteVolService.addPoints(vol, pointsVolConfig, r.getDateReservation());
                        totalVolPoints += pointsVolConfig;
                    }
                }

                // Create billet if not exists
                boolean billetExists = billetRepository.findAll().stream()
                        .anyMatch(b -> b.getReservation() != null && b.getReservation().getId().equals(r.getId()));
                if (!billetExists) {
                    Billet b = new Billet();
                    b.setReservation(r);
                    b.setSiege(r.getSiege());
                    b.setDateEmission(java.time.LocalDateTime.now());
                    String num = "BIL-" + r.getId() + "-" + System.currentTimeMillis();
                    b.setNumeroBillet(num);
                    b.setPrixFinal(r.getPrix());
                    java.util.List<StatusBillet> statuses = statusBilletRepository.findAll();
                    if (statuses.size() > 4) b.setStatusBillet(statuses.get(4));
                    else if (!statuses.isEmpty()) b.setStatusBillet(statuses.get(0));
                    billetRepository.save(b);
                }
            }

            String message = confirmedCount + " réservation(s) confirmée(s) et payée(s)";
            if (totalPoints > 0) {
                message += " - " + totalPoints + " points de fidélité ajoutés au total";
            }
            if (totalVolPoints > 0) {
                message += " - " + totalVolPoints + " points de popularité vol ajoutés";
            }
            redirect.addFlashAttribute("successMessage", message);
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la confirmation: " + ex.getMessage());
        }
        return "redirect:/reservations";
    }
}
