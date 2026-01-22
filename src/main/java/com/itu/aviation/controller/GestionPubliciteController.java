package com.itu.aviation.controller;

import com.itu.aviation.entity.Publicite;
import com.itu.aviation.entity.PubliciteVideo;
import com.itu.aviation.entity.Societe;
import com.itu.aviation.service.DiffusionService;
import com.itu.aviation.service.PubliciteVideoService;
import com.itu.aviation.service.SocieteService;
import com.itu.aviation.service.VolInstanceService;
import com.itu.aviation.service.ConfigService;
import com.itu.aviation.service.CaisseService;
import com.itu.aviation.service.PaiementPubliciteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/publicites")
public class GestionPubliciteController {

    private final SocieteService societeService;
    private final PubliciteVideoService publiciteVideoService;
    private final DiffusionService diffusionService;
    private final VolInstanceService volInstanceService;
    private final ConfigService configService;
    private final CaisseService caisseService;
    private final PaiementPubliciteService paiementPubliciteService;

    public GestionPubliciteController(SocieteService societeService, 
                                      PubliciteVideoService publiciteVideoService,
                                      DiffusionService diffusionService,
                                      VolInstanceService volInstanceService,
                                      ConfigService configService,
                                      CaisseService caisseService,
                                      PaiementPubliciteService paiementPubliciteService) {
        this.societeService = societeService;
        this.publiciteVideoService = publiciteVideoService;
        this.diffusionService = diffusionService;
        this.volInstanceService = volInstanceService;
        this.configService = configService;
        this.caisseService = caisseService;
        this.paiementPubliciteService = paiementPubliciteService;
    }

    @GetMapping
    public String index() {
        return "redirect:/publicites/diffusions";
    }

    @GetMapping("/societes")
    public ModelAndView societes(@RequestParam(value = "id", required = false) Long id) {
        ModelAndView mav = new ModelAndView("publicite/societes");
        mav.addObject("pageTitle", "Gestion des Sociétés publicitaires");
        mav.addObject("currentPage", "publicite-societes");
        mav.addObject("societes", societeService.findAll());
        Societe formSociete = id != null ? societeService.findById(id) : new Societe();
        mav.addObject("societe", formSociete);
        mav.addObject("isEdit", id != null);
        return mav;
    }

    @PostMapping("/societes/save")
    public String saveSociete(@ModelAttribute Societe societe, RedirectAttributes redirect) {
        try {
            if (societe.getId() == null) {
                societeService.create(societe);
                redirect.addFlashAttribute("successMessage", "Société créée avec succès");
            } else {
                societeService.update(societe.getId(), societe);
                redirect.addFlashAttribute("successMessage", "Société mise à jour avec succès");
            }
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/societes";
    }

    @PostMapping("/societes/delete/{id}")
    public String deleteSociete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            societeService.delete(id);
            redirect.addFlashAttribute("successMessage", "Société supprimée avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/societes";
    }

    @GetMapping("/videos")
    public ModelAndView videos(@RequestParam(value = "id", required = false) Long id) {
        ModelAndView mav = new ModelAndView("publicite/videos");
        mav.addObject("pageTitle", "Gestion des Vidéos publicitaires");
        mav.addObject("currentPage", "publicite-videos");
        mav.addObject("videos", publiciteVideoService.findAll());
        PubliciteVideo formVideo = id != null ? publiciteVideoService.findById(id) : new PubliciteVideo();
        mav.addObject("video", formVideo);
        mav.addObject("isEdit", id != null);
        mav.addObject("societes", societeService.findAll());
        return mav;
    }

    @PostMapping("/videos/save")
    public String saveVideo(@ModelAttribute PubliciteVideo publiciteVideo, RedirectAttributes redirect) {
        try {
            boolean isCreate = publiciteVideo.getId() == null;
            publiciteVideoService.save(publiciteVideo);
            redirect.addFlashAttribute("successMessage", isCreate ?
                    "Vidéo créée avec succès" : "Vidéo mise à jour avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/videos";
    }

    @PostMapping("/videos/delete/{id}")
    public String deleteVideo(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            publiciteVideoService.delete(id);
            redirect.addFlashAttribute("successMessage", "Vidéo supprimée avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/videos";
    }

    // ============== DIFFUSIONS ==============

    @GetMapping("/diffusions")
    public ModelAndView diffusions(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        
        ModelAndView mav = new ModelAndView("publicite/diffusions");
        mav.addObject("pageTitle", "Gestion des Diffusions");
        mav.addObject("currentPage", "publicite-diffusions");

        // Valeurs par défaut
        int currentYear = year != null ? year : LocalDate.now().getYear();
        int currentMonth = month != null ? month : 0; // 0 = tous les mois

        List<Publicite> diffusions = diffusionService.findByYearAndMonth(currentYear, currentMonth);
        mav.addObject("diffusions", diffusions);

        // CA
        Double caReel = diffusionService.getCAReelByPeriode(currentYear, currentMonth);
        Double caTheorique = diffusionService.getCATheoriqueByPeriode(currentYear, currentMonth);
        mav.addObject("caReel", caReel);
        mav.addObject("caTheorique", caTheorique);

        // Filtres
        mav.addObject("selectedYear", currentYear);
        mav.addObject("selectedMonth", currentMonth);
        mav.addObject("coutConfig", configService.getCoutPubliciter());

        // Données pour le formulaire
        mav.addObject("societes", societeService.findAll());
        mav.addObject("videos", publiciteVideoService.findAll());
        mav.addObject("volInstances", volInstanceService.findAll());

        return mav;
    }

    @PostMapping("/diffusions/save")
    public String saveDiffusion(
            @RequestParam("societeId") Long societeId,
            @RequestParam(value = "publiciteVideoId", required = false) Long publiciteVideoId,
            @RequestParam("volInstanceId") Long volInstanceId,
            @RequestParam(value = "nombreFois", defaultValue = "1") int nombreFois,
            RedirectAttributes redirect) {
        try {
            diffusionService.createMultiple(societeId, publiciteVideoId, volInstanceId, nombreFois);
            redirect.addFlashAttribute("successMessage", nombreFois + " diffusion(s) créée(s) avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/diffusions";
    }

    @PostMapping("/diffusions/delete/{id}")
    public String deleteDiffusion(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            diffusionService.delete(id);
            redirect.addFlashAttribute("successMessage", "Diffusion supprimée avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/diffusions";
    }

    // ==================== PAIEMENTS ====================

    @GetMapping("/paiements")
    public ModelAndView listPaiements(
            @RequestParam(value = "societeId", required = false) Long societeId) {
        ModelAndView mav = new ModelAndView("publicite/paiements");
        
        // Bilan de toutes les sociétés
        List<PaiementPubliciteService.SocieteBilan> bilans = paiementPubliciteService.getBilanToutesSocietes();
        mav.addObject("bilans", bilans);
        
        // Paiements (tous ou par société)
        if (societeId != null) {
            mav.addObject("paiements", paiementPubliciteService.getPaiementsBySociete(societeId));
            mav.addObject("selectedSocieteId", societeId);
        } else {
            mav.addObject("paiements", paiementPubliciteService.findAll());
        }
        
        // Données pour le formulaire
        mav.addObject("societes", societeService.findAll());
        mav.addObject("caisses", caisseService.findAll());
        
        return mav;
    }

    @PostMapping("/paiements/save")
    public String savePaiement(
            @RequestParam("societeId") Long societeId,
            @RequestParam("montant") BigDecimal montant,
            @RequestParam("caisseId") Long caisseId,
            @RequestParam(value = "reference", required = false) String reference,
            @RequestParam(value = "datePaiement", required = false) String datePaiementStr,
            RedirectAttributes redirect) {
        try {
            java.time.LocalDateTime datePaiement = null;
            if (datePaiementStr != null && !datePaiementStr.isEmpty()) {
                datePaiement = java.time.LocalDateTime.parse(datePaiementStr + "T00:00:00");
            }
            paiementPubliciteService.creerPaiement(societeId, montant, caisseId, reference, datePaiement);
            redirect.addFlashAttribute("successMessage", "Paiement enregistré avec succès");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/publicites/paiements";
    }

    @GetMapping("/paiements/reste/{societeId}")
    @ResponseBody
    public BigDecimal getResteAPayer(@PathVariable Long societeId) {
        return paiementPubliciteService.getResteAPayerBySociete(societeId);
    }
}
