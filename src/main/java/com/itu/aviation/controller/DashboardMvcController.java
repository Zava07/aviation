package com.itu.aviation.controller;

import com.itu.aviation.service.*;
import com.itu.aviation.entity.Vol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardMvcController {

    @Autowired
    private VolService volService;

    @Autowired
    private AvionService avionService;

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private BilletService billetService;

    @Autowired
    private PaiementService paiementService;

    @GetMapping({"/", "/dashboard"})
    public ModelAndView dashboard() {
        ModelAndView mav = new ModelAndView("dashboard");

        List<Vol> vols = volService.findAll();
        mav.addObject("totalVols", vols.size());
        mav.addObject("totalAvions", avionService.findAll().size());
        mav.addObject("totalPassagers", passagerService.findAll().size());
        mav.addObject("totalReservations", reservationService.findAll().size());
        mav.addObject("totalBillets", billetService.findAll().size());
        mav.addObject("totalPaiements", paiementService.findAll().size());

        List<Vol> recent = vols.stream()
                .filter(v -> v.getDateDepart() != null)
                .sorted(Comparator.comparing(Vol::getDateDepart).reversed())
                .limit(5)
                .collect(Collectors.toList());
        mav.addObject("recentVols", recent);

        mav.addObject("pageTitle", "Tableau de Bord");
        mav.addObject("pageDescription", "Statistiques rapides et vols récents");
        mav.addObject("currentPage", "dashboard");

        return mav;
    }
}
