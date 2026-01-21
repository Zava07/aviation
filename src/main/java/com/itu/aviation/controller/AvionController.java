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
import java.util.List;
import java.util.stream.Collectors;
 

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

    @Autowired
    private com.itu.aviation.service.VolInstanceService volInstanceService;

    @Autowired
    private com.itu.aviation.service.PrixVolClasseService prixVolClasseService;

    @Autowired
    private com.itu.aviation.service.ReservationService reservationService;

    @Autowired
    private com.itu.aviation.repository.BilletRepository billetRepository;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String codeAvion,
                             @RequestParam(required = false) Long modeleId,
                             @RequestParam(required = false) Integer minCap,
                             @RequestParam(required = false) Integer maxCap,
                             @RequestParam(required = false) Long etatId) {
        ModelAndView mav = new ModelAndView("avions/list");
        mav.addObject("pageTitle", "Avions");
        mav.addObject("currentPage", "avions");
        List<com.itu.aviation.entity.Avion> all = avionService.findAll();
        List<com.itu.aviation.entity.Avion> filtered = all.stream().filter(a -> {
            if (codeAvion != null && !codeAvion.isBlank()) {
                if (a.getCodeAvion() == null || !a.getCodeAvion().toLowerCase().contains(codeAvion.toLowerCase())) return false;
            }
            if (modeleId != null) {
                if (a.getModeleAvion() == null || !modeleId.equals(a.getModeleAvion().getId())) return false;
            }
            if (minCap != null) {
                if (a.getCapacitePassagers() == null || a.getCapacitePassagers() < minCap) return false;
            }
            if (maxCap != null) {
                if (a.getCapacitePassagers() == null || a.getCapacitePassagers() > maxCap) return false;
            }
            if (etatId != null) {
                if (a.getEtatAvion() == null || !etatId.equals(a.getEtatAvion().getId())) return false;
            }
            return true;
        }).collect(Collectors.toList());
        mav.addObject("avions", filtered);
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

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("avions/details");
        mav.addObject("pageTitle", "Détails Avion");
        mav.addObject("currentPage", "avions");
        com.itu.aviation.entity.Avion avion = avionService.findById(id);
        mav.addObject("avion", avion);

        java.util.List<com.itu.aviation.entity.Siege> sieges = siegeService.findAll().stream()
                .filter(s -> s.getAvion() != null && s.getAvion().getId() != null && s.getAvion().getId().equals(id))
                .toList();
        mav.addObject("sieges", sieges);

        // collect distinct Vols for which this avion has at least one VolInstance
        java.util.List<com.itu.aviation.entity.Vol> vols = volInstanceService.findAll().stream()
            .filter(v -> v.getAvion() != null && v.getAvion().getId() != null && v.getAvion().getId().equals(id))
            .map(v -> v.getVol())
            .filter(v -> v != null && v.getId() != null)
            .distinct()
            .toList();

        java.util.List<java.util.Map<String, Object>> volsWithMax = new java.util.ArrayList<>();
        java.util.List<com.itu.aviation.entity.PrixVolClasse> allPrices = prixVolClasseService.findAll();

        // precompute seat counts per classe for this avion
        java.util.Map<Long, Long> seatCountByClasse = new java.util.HashMap<>();
        for (com.itu.aviation.entity.Siege s : sieges) {
            if (s.getClasse() == null || s.getClasse().getId() == null) continue;
            Long cid = s.getClasse().getId();
            seatCountByClasse.put(cid, seatCountByClasse.getOrDefault(cid, 0L) + 1L);
        }

        // prepare a list of classes with counts for dashboard
        java.util.List<java.util.Map<String, Object>> seatCounts = new java.util.ArrayList<>();
        java.util.List<com.itu.aviation.entity.Classe> allClasses = classeService.findAll();
        for (com.itu.aviation.entity.Classe c : allClasses) {
            java.util.Map<String, Object> m = new java.util.HashMap<>();
            m.put("classe", c);
            m.put("count", seatCountByClasse.getOrDefault(c.getId(), 0L));
            seatCounts.add(m);
        }
        mav.addObject("seatCounts", seatCounts);

        for (com.itu.aviation.entity.Vol vol : vols) {
            java.math.BigDecimal max = java.math.BigDecimal.ZERO;
            Long volId = vol.getId();
            for (java.util.Map.Entry<Long, Long> e : seatCountByClasse.entrySet()) {
                Long classeId = e.getKey();
                Long count = e.getValue();
                // Use only prices for category 'Adulte' when computing max revenue
                java.util.Optional<com.itu.aviation.entity.PrixVolClasse> p = allPrices.stream()
                    .filter(x -> x.getVol() != null && x.getVol().getId() != null && x.getVol().getId().equals(volId)
                        && x.getClasse() != null && x.getClasse().getId() != null && x.getClasse().getId().equals(classeId)
                        && (x.getCategoriePassager() == null || "Adulte".equalsIgnoreCase(x.getCategoriePassager().getCategorie())))
                    .findFirst();
                java.math.BigDecimal prix = p.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(java.math.BigDecimal.ZERO);
                max = max.add(prix.multiply(new java.math.BigDecimal(count)));
            }
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("vol", vol);
            map.put("maxRevenue", max);
            // gather prices per class for this vol to show in the view
            // show only prices for default category (Adulte) or prices without specific category
            java.util.List<com.itu.aviation.entity.PrixVolClasse> pricesForVol = allPrices.stream()
                .filter(x -> x.getVol() != null && x.getVol().getId() != null && x.getVol().getId().equals(volId))
                .filter(x -> x.getCategoriePassager() == null || "Adulte".equalsIgnoreCase(x.getCategoriePassager().getCategorie()))
                .toList();
            map.put("prixParClasse", pricesForVol);
            volsWithMax.add(map);
        }
        mav.addObject("volsWithMax", volsWithMax);

        // list vol instances for this avion (convert LocalDateTime -> java.util.Date for JSP fmt:formatDate)
        java.util.List<java.util.Map<String, Object>> volInstancesView = volInstanceService.findAll().stream()
                .filter(vi -> vi.getAvion() != null && vi.getAvion().getId() != null && vi.getAvion().getId().equals(id))
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
                    m.put("dateDepart", vi.getDateDepart());
                    m.put("dateArrivee", vi.getDateArrivee());
                    java.util.Date dateDepartDate = null;
                    java.util.Date dateArriveeDate = null;
                    if (vi.getDateDepart() != null) {
                        dateDepartDate = java.util.Date.from(vi.getDateDepart().atZone(java.time.ZoneId.systemDefault()).toInstant());
                    }
                    if (vi.getDateArrivee() != null) {
                        dateArriveeDate = java.util.Date.from(vi.getDateArrivee().atZone(java.time.ZoneId.systemDefault()).toInstant());
                    }
                    m.put("dateDepartDate", dateDepartDate);
                    m.put("dateArriveeDate", dateArriveeDate);

                    // compute total price for billets 'Payer'/'Emis' for this vol instance
                    java.math.BigDecimal totalPaidEmis = java.math.BigDecimal.ZERO;
                    try {
                        java.util.List<com.itu.aviation.entity.Reservation> allRes = reservationService.findAll();
                        for (com.itu.aviation.entity.Reservation rr : allRes) {
                            if (rr.getVol() != null && rr.getVol().getId() != null && rr.getVol().getId().equals(vi.getId()) && rr.getPrix() != null) {
                                boolean hasPaidOrEmis = billetRepository.findAll().stream().anyMatch(b ->
                                    b.getReservation() != null && b.getReservation().getId() != null && b.getReservation().getId().equals(rr.getId())
                                    && b.getStatusBillet() != null && b.getStatusBillet().getStatus() != null
                                    && (b.getStatusBillet().getStatus().toLowerCase().contains("payer") || b.getStatusBillet().getStatus().toLowerCase().contains("emis"))
                                );
                                if (hasPaidOrEmis) totalPaidEmis = totalPaidEmis.add(rr.getPrix());
                            }
                        }
                    } catch (Exception ex) {
                        // ignore errors and leave total as zero
                    }
                    m.put("totalPaidEmis", totalPaidEmis);
                    return m;
                }).toList();
        mav.addObject("volInstances", volInstancesView);

        return mav;
    }
}
