package com.itu.aviation.controller;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.entity.Caisse;
import com.itu.aviation.entity.MouvementCaisse;
import com.itu.aviation.entity.Paiement;
import com.itu.aviation.entity.Reservation;
import com.itu.aviation.entity.StatusBillet;
import com.itu.aviation.entity.StatusPaiement;
import com.itu.aviation.entity.TypeMouvementCaisse;
import com.itu.aviation.service.PaiementService;
import com.itu.aviation.service.ReservationService;
import com.itu.aviation.service.StatusBilletService;
import com.itu.aviation.service.BilletService;
import com.itu.aviation.service.DeviseService;
import com.itu.aviation.service.MethodePaiementService;
import com.itu.aviation.service.StatusPaiementService;
import com.itu.aviation.service.MouvementCaisseService;
import com.itu.aviation.service.CaisseService;
import com.itu.aviation.service.TypeMouvementCaisseService;
import com.itu.aviation.service.BilletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
 

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    

    @Autowired
    private DeviseService deviseService;

    @Autowired
    private MethodePaiementService methodePaiementService;

    @Autowired
    private StatusPaiementService statusPaiementService;

    @Autowired
    private MouvementCaisseService mouvementCaisseService;

    @Autowired
    private CaisseService caisseService;

    @Autowired
    private TypeMouvementCaisseService typeMouvementCaisseService;

    @Autowired
    private BilletService billetService;
    
    @Autowired
    ReservationService reservationService;

    @Autowired
    StatusBilletService statusBilletService;

    @ModelAttribute("reservations")
    public java.util.List<com.itu.aviation.entity.Reservation> populateReservations(){
        return reservationService.findAll();
    }

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) Long reservationId,
                             @RequestParam(required = false) Long statusId,
                             @RequestParam(required = false) Long methodeId,
                             @RequestParam(required = false) Long deviseId,
                             @RequestParam(required = false) Long caisseId,
                             @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateFrom,
                             @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateTo,
                             @RequestParam(required = false) java.math.BigDecimal montantMin,
                             @RequestParam(required = false) java.math.BigDecimal montantMax,
                             @RequestParam(required = false) String passager,
                             @RequestParam(required = false) java.util.List<String> sortFields,
                             @RequestParam(required = false) java.util.List<String> sortDirs) {
        ModelAndView mav = new ModelAndView("paiements/list");
        mav.addObject("pageTitle", "Paiements");
        mav.addObject("currentPage", "paiements");
        java.util.List<com.itu.aviation.entity.Paiement> all = paiementService.findAll();

        // build a quick map paiementId -> list of caisseIds to speed-up filtering
        java.util.List<com.itu.aviation.entity.MouvementCaisse> mouvements = mouvementCaisseService.findAll();
        java.util.Map<Long, java.util.List<Long>> paiementToCaisses = mouvements.stream()
            .filter(m -> m.getPaiement() != null && m.getPaiement().getId() != null && m.getCaisse() != null && m.getCaisse().getId() != null)
            .collect(Collectors.groupingBy(m -> m.getPaiement().getId(), Collectors.mapping(m -> m.getCaisse().getId(), Collectors.toList())));
        // expose mouvements and mapping to view to allow showing caisse(s) per paiement
        mav.addObject("mouvements", mouvements);
        mav.addObject("paiementToCaisses", paiementToCaisses);
        java.util.List<com.itu.aviation.entity.Paiement> filtered = all.stream().filter(p -> {
            if (reservationId != null) {
                if (p.getReservation() == null || !reservationId.equals(p.getReservation().getId())) return false;
            }
            if (statusId != null) {
                if (p.getStatusPaiement() == null || !statusId.equals(p.getStatusPaiement().getId())) return false;
            }
            if (methodeId != null) {
                if (p.getMethodePaiement() == null || !methodeId.equals(p.getMethodePaiement().getId())) return false;
            }
            if (deviseId != null) {
                if (p.getDevise() == null || !deviseId.equals(p.getDevise().getId())) return false;
            }
            if (caisseId != null) {
                java.util.List<Long> cs = paiementToCaisses.get(p.getId());
                if (cs == null || !cs.contains(caisseId)) return false;
            }
            if (montantMin != null) {
                if (p.getMontant() == null || p.getMontant().compareTo(montantMin) < 0) return false;
            }
            if (montantMax != null) {
                if (p.getMontant() == null || p.getMontant().compareTo(montantMax) > 0) return false;
            }
            if (passager != null && !passager.trim().isEmpty()) {
                if (p.getReservation() == null || p.getReservation().getPassager() == null) return false;
                String fullName = (p.getReservation().getPassager().getNom() == null ? "" : p.getReservation().getPassager().getNom()) + " " + (p.getReservation().getPassager().getPrenom() == null ? "" : p.getReservation().getPassager().getPrenom());
                if (!fullName.toLowerCase().contains(passager.toLowerCase())) return false;
            }
            if (dateFrom != null) {
                if (p.getDatePaiement() == null || p.getDatePaiement().toLocalDate().isBefore(dateFrom)) return false;
            }
            if (dateTo != null) {
                if (p.getDatePaiement() == null || p.getDatePaiement().toLocalDate().isAfter(dateTo)) return false;
            }
            return true;
        }).collect(Collectors.toList());
        // echo filter params back to view so the form can keep values
        mav.addObject("filterReservationId", reservationId);
        mav.addObject("filterStatusId", statusId);
        mav.addObject("filterMethodeId", methodeId);
        mav.addObject("filterDeviseId", deviseId);
        mav.addObject("filterDateFrom", dateFrom);
        mav.addObject("filterDateTo", dateTo);
        mav.addObject("filterMontantMin", montantMin);
        mav.addObject("filterMontantMax", montantMax);
        mav.addObject("filterPassager", passager);
        mav.addObject("filterCaisseId", caisseId);
        // echo filter params back to view so the form can keep values
        mav.addObject("filterReservationId", reservationId);
        mav.addObject("filterStatusId", statusId);
        mav.addObject("filterMethodeId", methodeId);
        mav.addObject("filterDeviseId", deviseId);
        mav.addObject("filterDateFrom", dateFrom);
        mav.addObject("filterDateTo", dateTo);
        mav.addObject("filterMontantMin", montantMin);
        mav.addObject("filterMontantMax", montantMax);
        mav.addObject("filterPassager", passager);
        // apply multi-criteria sorting if requested (sortFields and sortDirs)
        if (sortFields != null && !sortFields.isEmpty()) {
            java.util.Comparator<com.itu.aviation.entity.Paiement> comp = null;
            for (int i = 0; i < sortFields.size(); i++) {
                String f = sortFields.get(i);
                String dir = (sortDirs != null && sortDirs.size() > i) ? sortDirs.get(i) : "asc";
                java.util.Comparator<com.itu.aviation.entity.Paiement> c = null;
                switch (f) {
                    case "montant":
                        c = java.util.Comparator.comparing(p -> p.getMontant(), java.util.Comparator.nullsLast(java.math.BigDecimal::compareTo));
                        break;
                    case "date":
                        c = java.util.Comparator.comparing(p -> p.getDatePaiement(), java.util.Comparator.nullsLast(java.time.LocalDateTime::compareTo));
                        break;
                    case "status":
                        c = java.util.Comparator.comparing(p -> p.getStatusPaiement() != null ? p.getStatusPaiement().getStatus() : "", java.util.Comparator.nullsLast(String::compareToIgnoreCase));
                        break;
                    case "methode":
                        c = java.util.Comparator.comparing(p -> p.getMethodePaiement() != null ? p.getMethodePaiement().getNomMethode() : "", java.util.Comparator.nullsLast(String::compareToIgnoreCase));
                        break;
                    case "devise":
                        c = java.util.Comparator.comparing(p -> p.getDevise() != null ? p.getDevise().getNomDevise() : "", java.util.Comparator.nullsLast(String::compareToIgnoreCase));
                        break;
                    default:
                        c = java.util.Comparator.comparing(p -> p.getId());
                }
                if ("desc".equalsIgnoreCase(dir)) c = c.reversed();
                comp = (comp == null) ? c : comp.thenComparing(c);
            }
            if (comp != null) filtered.sort(comp);
        }

        mav.addObject("paiements", filtered);
        mav.addObject("billets", billetService.findAll());
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("methodes", methodePaiementService.findAll());
        mav.addObject("statusList", statusPaiementService.findAll());
        mav.addObject("caisses", caisseService.findAll());
        // add reservations for create modal reservation selector
        mav.addObject("reservations", reservationService.findAll());
        // echo sort parameters back to view
        mav.addObject("sortFields", sortFields);
        mav.addObject("sortDirs", sortDirs);
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
    public ModelAndView create(@RequestParam(required = false) Long reservationId,
                                @RequestParam(required = false) Long billetId) {
        // Redirect to list view and open the create modal there
        ModelAndView mav = new ModelAndView("paiements/list");
        mav.addObject("pageTitle", "Payer Billet");
        mav.addObject("currentPage", "paiements");
        mav.addObject("openCreateMultiModal", true);
        if (reservationId != null) {
            mav.addObject("reservation", reservationService.findById(reservationId));
        }
        if (billetId != null) {
            com.itu.aviation.entity.Billet b = billetService.findById(billetId);
            mav.addObject("billet", b);
            if (b.getReservation() != null) {
                mav.addObject("reservation", b.getReservation());
            }
        }
        mav.addObject("billets", billetService.findAll());
        mav.addObject("devises", deviseService.findAll());
        mav.addObject("methodes", methodePaiementService.findAll());
        mav.addObject("statusList", statusPaiementService.findAll());
        mav.addObject("caisses", caisseService.findAll());
        // also include list data for filters
        mav.addObject("paiements", paiementService.findAll());
        mav.addObject("reservations", reservationService.findAll());
        return mav;
    }

    @GetMapping("/caisses/by-methode/{methodeId}")
    @ResponseBody
    public java.util.List<java.util.Map<String,Object>> getCaissesByMethode(@PathVariable Long methodeId) {
        if (methodeId == null) return java.util.Collections.emptyList();
        java.util.List<com.itu.aviation.entity.Caisse> cs = caisseService.findByMethodeId(methodeId);
        return cs.stream().map(c -> java.util.Map.<String,Object>of("id", c.getId(), "nom", c.getNomCaisse())).collect(Collectors.toList());
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

    @PostMapping("/save-single")
    public String saveSingle(@RequestParam(required = false) Long billetId,
                             @RequestParam(required = false) String billetNumero,
                             @RequestParam(required = false) java.math.BigDecimal montant,
                             @RequestParam(required = false) Long methodeId,
                             @RequestParam(required = false) Long deviseId,
                             @RequestParam(required = false) Long statusId,
                             @RequestParam(required = false) Long caisseId,
                             RedirectAttributes redirect) {
        try {
            Billet b = null;
            if (billetId != null) b = billetService.findById(billetId);
            else if (billetNumero != null && !billetNumero.trim().isEmpty()) b = billetService.findByNumero(billetNumero.trim());
            if (b == null) {
                redirect.addFlashAttribute("errorMessage", "Billet introuvable");
                return "redirect:/paiements";
            }
            Reservation res = b.getReservation();
            BigDecimal amt = montant != null ? montant : b.getPrixFinal();
            if (amt == null) {
                redirect.addFlashAttribute("errorMessage", "Montant manquant");
                return "redirect:/paiements";
            }
            List<Paiement> existingPaiements = paiementService.findByBilletId(b.getId());
            if(existingPaiements != null && !existingPaiements.isEmpty()) {
                Paiement p = existingPaiements.get(0);
                List<MouvementCaisse> totalMouv = mouvementCaisseService.findByPaiementId(p.getId());
                BigDecimal totalPaid = BigDecimal.ZERO;
                for(MouvementCaisse m : totalMouv) {
                    if(m.getMontant() != null) {
                        totalPaid = totalPaid.add(m.getMontant());
                    }
                }
                if (totalPaid.compareTo(b.getPrixFinal()) >= 0) {
                    redirect.addFlashAttribute("errorMessage", "Le billet a déjà été entièrement payé.");
                    return "redirect:/paiements";
                }else{
                    BigDecimal reste = b.getPrixFinal().subtract(totalPaid);
                    BigDecimal retour = amt.subtract(reste);

                    BigDecimal reste_a_payer_after_payement = p.getMontant().subtract(totalPaid.add(amt));
                    MouvementCaisse m = new MouvementCaisse();
                    m.setPaiement(p);
                    m.setCaisse(caisseService.findById(caisseId));
                    m.setMontant(amt);
                    List<TypeMouvementCaisse> types = typeMouvementCaisseService.findAll();
                    Optional<TypeMouvementCaisse> creditTypeOpt = types.stream().filter(t -> "credit".equalsIgnoreCase(t.getType())).findFirst();
                    creditTypeOpt.ifPresent(m::setTypeMouvementCaisse);
                    m.setDateMouvement(LocalDateTime.now());
                    MouvementCaisse saved = mouvementCaisseService.create(m);
                    Caisse c = saved.getCaisse();
                    if (c != null) {
                        String t = saved.getTypeMouvementCaisse() != null ? saved.getTypeMouvementCaisse().getType() : "";
                        java.math.BigDecimal delta = amt != null ? amt : java.math.BigDecimal.ZERO;
                        java.math.BigDecimal current = c.getSolde() != null ? c.getSolde() : java.math.BigDecimal.ZERO;
                        java.math.BigDecimal newSolde = "debit".equalsIgnoreCase(t) ? current.subtract(delta) : current.add(delta);
                        c.setSolde(newSolde);
                        c.setDateLastMouv(LocalDateTime.now());
                        caisseService.update(c.getId(), c);
                    }
                    if(amt.compareTo(reste) > 0) {
                        List<StatusBillet> statusBillets = statusBilletService.findAll();
                        b.setStatusBillet(statusBillets.get(0));
                        billetService.update(b.getId(), b);
                        redirect.addFlashAttribute("successMessage", "Paiement enregistré avec une remise de " + reste_a_payer_after_payement.abs() + " car le billet a été entièrement payé.");
                        return "redirect:/paiements";
                    }else{
                        redirect.addFlashAttribute("successMessage", "Paiement enregistré avec une reste a payer de " + retour.abs() + " pour ce billet.");
                        return "redirect:/paiements";
                    }
                }
            }
            Paiement p = new Paiement();
            p.setReservation(res);
            p.setBillet(b);
            p.setMontant(res.getPrix());
            if (methodeId != null) p.setMethodePaiement(methodePaiementService.findById(methodeId));
            if (deviseId != null) p.setDevise(deviseService.findById(deviseId));
            if (statusId != null) p.setStatusPaiement(statusPaiementService.findById(statusId));
            else {
                List<StatusPaiement> sl = statusPaiementService.findAll();
                if (!sl.isEmpty()) p.setStatusPaiement(sl.get(1));
            }
            p.setDatePaiement(LocalDateTime.now());
            paiementService.create(p);

            // create mouvement caisse if caisse selected
            if (caisseId != null) {
                MouvementCaisse m = new MouvementCaisse();
                m.setPaiement(p);
                m.setCaisse(caisseService.findById(caisseId));
                m.setMontant(amt);
                List<TypeMouvementCaisse> types = typeMouvementCaisseService.findAll();
                Optional<TypeMouvementCaisse> creditTypeOpt = types.stream().filter(t -> "credit".equalsIgnoreCase(t.getType())).findFirst();
                creditTypeOpt.ifPresent(m::setTypeMouvementCaisse);
                m.setDateMouvement(LocalDateTime.now());
                MouvementCaisse saved2 = mouvementCaisseService.create(m);
                Caisse c = saved2.getCaisse();
                if (c != null) {
                    String t = saved2.getTypeMouvementCaisse() != null ? saved2.getTypeMouvementCaisse().getType() : "";
                    java.math.BigDecimal delta = amt != null ? amt : java.math.BigDecimal.ZERO;
                    java.math.BigDecimal current = c.getSolde() != null ? c.getSolde() : java.math.BigDecimal.ZERO;
                    java.math.BigDecimal newSolde = "debit".equalsIgnoreCase(t) ? current.subtract(delta) : current.add(delta);
                    c.setSolde(newSolde);
                    c.setDateLastMouv(LocalDateTime.now());
                    caisseService.update(c.getId(), c);
                }
            }
            if(amt.compareTo(b.getPrixFinal()) >= 0) {
                List<StatusBillet> statusBillets = statusBilletService.findAll();
                b.setStatusBillet(statusBillets.get(0));
                billetService.update(b.getId(), b);
            }
            redirect.addFlashAttribute("successMessage", "Paiement enregistré");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors du paiement: " + ex.getMessage());
        }
        return "redirect:/paiements";
    }

    @PostMapping("/pay-all-billets")
    public String payAllBillets(@RequestParam Long methodeId,
                                @RequestParam Long deviseId,
                                @RequestParam Long caisseId,
                                RedirectAttributes redirect) {
        try {
            List<Billet> allBillets = billetService.findAll();
            List<StatusBillet> statusBillets = statusBilletService.findAll();
            StatusBillet payeStatus = statusBillets.stream()
                .filter(s -> s.getStatus() != null && s.getStatus().toLowerCase().contains("pay"))
                .findFirst().orElse(statusBillets.isEmpty() ? null : statusBillets.get(0));
            
            List<StatusPaiement> statusPaiementList = statusPaiementService.findAll();
            StatusPaiement payeStatusPaiement = statusPaiementList.size() > 1 ? statusPaiementList.get(1) : (statusPaiementList.isEmpty() ? null : statusPaiementList.get(0));
            
            List<TypeMouvementCaisse> types = typeMouvementCaisseService.findAll();
            TypeMouvementCaisse creditType = types.stream()
                .filter(t -> "credit".equalsIgnoreCase(t.getType()))
                .findFirst().orElse(null);
            
            Caisse caisse = caisseService.findById(caisseId);
            int count = 0;
            
            for (Billet b : allBillets) {
                // verifier si le billet est deja paye
                if (b.getStatusBillet() != null && b.getStatusBillet().getStatus() != null 
                    && b.getStatusBillet().getStatus().toLowerCase().contains("pay")) {
                    continue;
                }
                
                // verifier si paiement existe deja
                List<Paiement> existingPaiements = paiementService.findByBilletId(b.getId());
                if (existingPaiements != null && !existingPaiements.isEmpty()) {
                    // verifier si totalement paye
                    Paiement existingP = existingPaiements.get(0);
                    List<MouvementCaisse> mouvs = mouvementCaisseService.findByPaiementId(existingP.getId());
                    BigDecimal totalPaid = BigDecimal.ZERO;
                    for (MouvementCaisse m : mouvs) {
                        if (m.getMontant() != null) totalPaid = totalPaid.add(m.getMontant());
                    }
                    if (b.getPrixFinal() != null && totalPaid.compareTo(b.getPrixFinal()) >= 0) {
                        continue; // deja paye
                    }
                }
                
                BigDecimal montant = b.getPrixFinal();
                if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) continue;
                
                Reservation res = b.getReservation();
                
                // creer paiement
                Paiement p = new Paiement();
                p.setReservation(res);
                p.setBillet(b);
                p.setMontant(montant);
                p.setMethodePaiement(methodePaiementService.findById(methodeId));
                p.setDevise(deviseService.findById(deviseId));
                p.setStatusPaiement(payeStatusPaiement);
                p.setDatePaiement(LocalDateTime.now());
                paiementService.create(p);
                
                // creer mouvement caisse
                MouvementCaisse m = new MouvementCaisse();
                m.setPaiement(p);
                m.setCaisse(caisse);
                m.setMontant(montant);
                m.setTypeMouvementCaisse(creditType);
                m.setDateMouvement(LocalDateTime.now());
                MouvementCaisse saved = mouvementCaisseService.create(m);
                
                // mettre a jour solde caisse
                if (caisse != null) {
                    BigDecimal current = caisse.getSolde() != null ? caisse.getSolde() : BigDecimal.ZERO;
                    caisse.setSolde(current.add(montant));
                    caisse.setDateLastMouv(LocalDateTime.now());
                    caisseService.update(caisse.getId(), caisse);
                }
                
                // mettre a jour status billet
                if (payeStatus != null) {
                    b.setStatusBillet(payeStatus);
                    billetService.update(b.getId(), b);
                }
                
                count++;
            }
            
            redirect.addFlashAttribute("successMessage", count + " billet(s) payé(s) avec succès");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors du paiement: " + ex.getMessage());
        }
        return "redirect:/billets";
    }

    @PostMapping("/rembourser/{billetId}")
    public String rembourserBillet(@PathVariable Long billetId,
                                   RedirectAttributes redirect) {
        try {
            Billet b = billetService.findById(billetId);
            if (b == null) {
                redirect.addFlashAttribute("errorMessage", "Billet introuvable");
                return "redirect:/billets";
            }
            java.math.BigDecimal montant = b.getPrixFinal();
            if (montant == null || montant.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                redirect.addFlashAttribute("errorMessage", "Montant invalide");
                return "redirect:/billets";
            }
            // Long caisseId = null;
            List<Paiement> paiements = paiementService.findByBilletId(billetId);
            if (paiements == null || paiements.isEmpty()) {
                redirect.addFlashAttribute("errorMessage", "Aucun paiement trouvé pour ce billet");
                return "redirect:/billets";
            }
            Paiement p = paiements.get(0);
            List<MouvementCaisse> mouvements = mouvementCaisseService.findByPaiementId(p.getId());
            if (mouvements == null || mouvements.isEmpty()) {
                redirect.addFlashAttribute("errorMessage", "Aucun mouvement de caisse trouvé pour le paiement de ce billet");
                return "redirect:/billets";
            }
            MouvementCaisse firstMouvement = mouvements.get(0);
            if (firstMouvement.getCaisse() == null || firstMouvement.getCaisse().getId() == null) {
                redirect.addFlashAttribute("errorMessage", "Le mouvement de caisse associé au paiement n'a pas de caisse valide");
                return "redirect:/billets";
            }
            long caisseId = firstMouvement.getCaisse().getId();
            Caisse c = caisseService.findById(caisseId);
            if (c == null) {
                redirect.addFlashAttribute("errorMessage", "Caisse introuvable");
                return "redirect:/billets";
            }
            java.math.BigDecimal solde = c.getSolde() != null ? c.getSolde() : java.math.BigDecimal.ZERO;
            if (solde.compareTo(montant) < 0) {
                redirect.addFlashAttribute("errorMessage", "Solde insuffisant dans la caisse sélectionnée");
                return "redirect:/billets";
            }

            // create debit movement
            MouvementCaisse m = new MouvementCaisse();
            m.setCaisse(c);
            m.setMontant(montant);
            m.setPaiement(p);
            java.util.List<TypeMouvementCaisse> types = typeMouvementCaisseService.findAll();
            java.util.Optional<TypeMouvementCaisse> debitTypeOpt = types.stream().filter(t -> "debit".equalsIgnoreCase(t.getType())).findFirst();
            debitTypeOpt.ifPresent(m::setTypeMouvementCaisse);
            m.setDateMouvement(LocalDateTime.now());
            MouvementCaisse saved = mouvementCaisseService.create(m);

            // update caisse solde
            Caisse caisseToUpdate = saved.getCaisse();
            if (caisseToUpdate != null) {
                java.math.BigDecimal current = caisseToUpdate.getSolde() != null ? caisseToUpdate.getSolde() : java.math.BigDecimal.ZERO;
                java.math.BigDecimal newSolde = current.subtract(montant);
                caisseToUpdate.setSolde(newSolde);
                caisseToUpdate.setDateLastMouv(LocalDateTime.now());
                caisseService.update(caisseToUpdate.getId(), caisseToUpdate);
            }

            // set billet status to remboursé if such status exists
            java.util.List<StatusBillet> statusList = statusBilletService.findAll();
            java.util.Optional<StatusBillet> rembOpt = statusList.stream().filter(s -> s.getStatus() != null && s.getStatus().toLowerCase().contains("remb")).findFirst();
            if (rembOpt.isPresent()) {
                b.setStatusBillet(rembOpt.get());
                billetService.update(b.getId(), b);
            }

            

            redirect.addFlashAttribute("successMessage", "Billet remboursé et mouvement enregistré");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors du remboursement: " + ex.getMessage());
        }
        return "redirect:/billets";
    }

}
