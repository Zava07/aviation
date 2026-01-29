package com.itu.aviation.controller;

import com.itu.aviation.entity.ProduitExtra;
import com.itu.aviation.service.ProduitExtraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/produits-extra")
public class ProduitExtraController {

    @Autowired
    private ProduitExtraService produitExtraService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String q) {
        ModelAndView mav = new ModelAndView("produitsextra/list");
        mav.addObject("pageTitle", "Produits Extra");
        mav.addObject("currentPage", "produitsextra");

        List<ProduitExtra> produits;
        if (q != null && !q.isBlank()) {
            produits = produitExtraService.search(q);
            mav.addObject("filterQ", q);
        } else {
            produits = produitExtraService.findAll();
        }

        mav.addObject("produits", produits);
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("produitsextra/form");
        mav.addObject("pageTitle", "Nouveau Produit Extra");
        mav.addObject("currentPage", "produitsextra");
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("produitsextra/form");
        mav.addObject("pageTitle", "Modifier Produit Extra");
        mav.addObject("currentPage", "produitsextra");
        mav.addObject("editProduit", produitExtraService.findById(id));
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProduitExtra produit, RedirectAttributes redirect) {
        try {
            if (produit.getId() == null) {
                produitExtraService.create(produit);
                redirect.addFlashAttribute("successMessage", "Produit créé avec succès");
            } else {
                produitExtraService.update(produit.getId(), produit);
                redirect.addFlashAttribute("successMessage", "Produit mis à jour");
            }
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur: " + ex.getMessage());
        }
        return "redirect:/produits-extra";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            produitExtraService.delete(id);
            redirect.addFlashAttribute("successMessage", "Produit supprimé");
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + ex.getMessage());
        }
        return "redirect:/produits-extra";
    }
}
