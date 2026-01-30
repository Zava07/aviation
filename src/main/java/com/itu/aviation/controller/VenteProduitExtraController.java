package com.itu.aviation.controller;

import com.itu.aviation.entity.ProduitExtra;
import com.itu.aviation.entity.VenteProduitExtra;
import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.service.ProduitExtraService;
import com.itu.aviation.service.VenteProduitExtraService;
import com.itu.aviation.service.VolInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/ventes-produits-extra")
public class VenteProduitExtraController {

    @Autowired
    private VenteProduitExtraService venteProduitExtraService;

    @Autowired
    private ProduitExtraService produitExtraService;

    @Autowired
    private VolInstanceService volInstanceService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) Long produitId,
                             @RequestParam(required = false) Long volInstanceId,
                             @RequestParam(required = false) String date) {
        ModelAndView mav = new ModelAndView("ventesproduitsextra/list");
        mav.addObject("pageTitle", "Ventes Produits Extra");
        mav.addObject("currentPage", "ventesproduitsextra");

        List<VenteProduitExtra> ventes = venteProduitExtraService.findAll();

        // Filtrage
        if (produitId != null) {
            ventes = ventes.stream()
                    .filter(v -> v.getProduitExtra() != null && v.getProduitExtra().getId().equals(produitId))
                    .toList();
            mav.addObject("filterProduitId", produitId);
        }

        if (volInstanceId != null) {
            ventes = ventes.stream()
                    .filter(v -> v.getVolInstance() != null && v.getVolInstance().getId().equals(volInstanceId))
                    .toList();
            mav.addObject("filterVolInstanceId", volInstanceId);
        }

        if (date != null && !date.isBlank()) {
            try {
                LocalDate filterDate = LocalDate.parse(date);
                ventes = ventes.stream()
                        .filter(v -> v.getDateAchat() != null && v.getDateAchat().toLocalDate().equals(filterDate))
                        .toList();
                mav.addObject("filterDate", date);
            } catch (Exception ex) {
                // ignore invalid date
            }
        }

        // Calcul du total
        BigDecimal totalVentes = ventes.stream()
                .map(VenteProduitExtra::getPrixTotal)
                .filter(p -> p != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalQuantite = ventes.stream()
                .map(VenteProduitExtra::getQuantite)
                .filter(q -> q != null)
                .reduce(0, Integer::sum);

        mav.addObject("ventes", ventes);
        mav.addObject("totalVentes", totalVentes);
        mav.addObject("totalQuantite", totalQuantite);
        mav.addObject("produits", produitExtraService.findAll());
        mav.addObject("volInstances", volInstanceService.findAll());

        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create(@RequestParam(required = false) Long volInstanceId) {
        ModelAndView mav = new ModelAndView("ventesproduitsextra/form");
        mav.addObject("pageTitle", "Nouvelle Vente");
        mav.addObject("currentPage", "ventesproduitsextra");
        mav.addObject("produits", produitExtraService.findAll());
        mav.addObject("volInstances", volInstanceService.findAll());
        if (volInstanceId != null) {
            mav.addObject("preselectedVolInstanceId", volInstanceId);
        }
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("ventesproduitsextra/form");
        mav.addObject("pageTitle", "Modifier Vente");
        mav.addObject("currentPage", "ventesproduitsextra");
        mav.addObject("editVente", venteProduitExtraService.findById(id));
        mav.addObject("produits", produitExtraService.findAll());
        mav.addObject("volInstances", volInstanceService.findAll());
        return mav;
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) Long id,
                       @RequestParam Long produitExtraId,
                       @RequestParam Long volInstanceId,
                       @RequestParam Integer quantite,
                       @RequestParam(required = false) BigDecimal prixTotal,
                       RedirectAttributes redirect) {
        try {
            VenteProduitExtra vente = new VenteProduitExtra();
            if (id != null) {
                vente = venteProduitExtraService.findById(id);
            }

            ProduitExtra produit = new ProduitExtra();
            produit.setId(produitExtraId);
            vente.setProduitExtra(produit);

            VolInstance volInstance = new VolInstance();
            volInstance.setId(volInstanceId);
            vente.setVolInstance(volInstance);

            vente.setQuantite(quantite);
            if (prixTotal != null) {
                vente.setPrixTotal(prixTotal);
            }

            if (id == null) {
                venteProduitExtraService.create(vente);
                redirect.addFlashAttribute("successMessage", "Vente enregistrée avec succès");
            } else {
                venteProduitExtraService.update(id, vente);
                redirect.addFlashAttribute("successMessage", "Vente mise à jour");
            }
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur: " + ex.getMessage());
        }
        return "redirect:/ventes-produits-extra";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            venteProduitExtraService.delete(id);
            redirect.addFlashAttribute("successMessage", "Vente supprimée");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + ex.getMessage());
        }
        return "redirect:/ventes-produits-extra";
    }
}
