package com.itu.aviation.controller;

import com.itu.aviation.entity.PopulariteVol;
import com.itu.aviation.entity.Vol;
import com.itu.aviation.service.PopulariteVolService;
import com.itu.aviation.service.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/popularite-vols")
public class PopulariteVolController {

    @Autowired
    private PopulariteVolService populariteVolService;

    @Autowired
    private VolService volService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) Integer annee,
                             @RequestParam(required = false) Integer mois,
                             @RequestParam(required = false) Long volId) {
        ModelAndView mav = new ModelAndView("popularite/list");
        mav.addObject("pageTitle", "Popularité des Vols");
        mav.addObject("currentPage", "popularite");

        // Default to current year/month if not provided
        LocalDate now = LocalDate.now();
        Integer filterAnnee = annee != null ? annee : now.getYear();
        Integer filterMois = mois; // Can be null for yearly view

        mav.addObject("filterAnnee", filterAnnee);
        mav.addObject("filterMois", filterMois);
        mav.addObject("filterVolId", volId);

        // Get all vols for filter dropdown
        List<Vol> vols = volService.findAll();
        mav.addObject("vols", vols);

        // Build ranking based on filter
        List<Map<String, Object>> ranking = new ArrayList<>();

        if (volId != null) {
            // Show history for specific vol
            Vol vol = volService.findById(volId);
            List<PopulariteVol> history;
            if (filterMois != null) {
                history = populariteVolService.findByMonth(filterAnnee, filterMois);
                history = history.stream().filter(p -> p.getVol().getId().equals(volId)).toList();
            } else {
                history = populariteVolService.findByYear(filterAnnee);
                history = history.stream().filter(p -> p.getVol().getId().equals(volId)).toList();
            }
            mav.addObject("selectedVol", vol);
            mav.addObject("volHistory", history);
            mav.addObject("volTotalPoints", vol.getPointVol());
        } else {
            // Show ranking
            List<Object[]> rankingData;
            if (filterMois != null) {
                rankingData = populariteVolService.getRankingByMonth(filterAnnee, filterMois);
            } else {
                rankingData = populariteVolService.getRankingByYear(filterAnnee);
            }

            int position = 1;
            for (Object[] row : rankingData) {
                Long vId = (Long) row[0];
                Long points = (Long) row[1];
                try {
                    Vol vol = volService.findById(vId);
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("position", position++);
                    entry.put("vol", vol);
                    entry.put("pointsPeriode", points);
                    entry.put("pointsTotal", vol.getPointVol());
                    ranking.add(entry);
                } catch (Exception e) {
                    // Vol not found, skip
                }
            }
        }

        mav.addObject("ranking", ranking);

        // Generate year options (last 5 years)
        List<Integer> years = new ArrayList<>();
        for (int y = now.getYear(); y >= now.getYear() - 4; y--) {
            years.add(y);
        }
        mav.addObject("years", years);

        // Month options
        String[] monthNames = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
        mav.addObject("monthNames", monthNames);

        // Global ranking (all time)
        List<Object[]> globalData = populariteVolService.getGlobalRanking();
        List<Map<String, Object>> globalRanking = new ArrayList<>();
        int gPos = 1;
        for (Object[] row : globalData) {
            Long vId = (Long) row[0];
            Long points = (Long) row[1];
            try {
                Vol vol = volService.findById(vId);
                Map<String, Object> entry = new HashMap<>();
                entry.put("position", gPos++);
                entry.put("vol", vol);
                entry.put("points", points);
                globalRanking.add(entry);
            } catch (Exception e) {
                // Vol not found, skip
            }
            if (gPos > 10) break; // Top 10
        }
        mav.addObject("globalRanking", globalRanking);

        return mav;
    }

    @GetMapping("/detail/{volId}")
    public ModelAndView detail(@PathVariable Long volId) {
        ModelAndView mav = new ModelAndView("popularite/detail");
        mav.addObject("pageTitle", "Détail Popularité Vol");
        mav.addObject("currentPage", "popularite");

        Vol vol = volService.findById(volId);
        mav.addObject("vol", vol);

        List<PopulariteVol> history = populariteVolService.findByVolId(volId);
        mav.addObject("history", history);
        mav.addObject("totalPoints", vol.getPointVol());

        // Points by month for current year
        LocalDate now = LocalDate.now();
        List<Map<String, Object>> monthlyPoints = new ArrayList<>();
        String[] monthNames = {"Jan", "Fév", "Mar", "Avr", "Mai", "Juin",
                "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
        for (int m = 1; m <= 12; m++) {
            Integer pts = populariteVolService.getPointsByMonth(volId, now.getYear(), m);
            Map<String, Object> mp = new HashMap<>();
            mp.put("mois", monthNames[m - 1]);
            mp.put("points", pts != null ? pts : 0);
            monthlyPoints.add(mp);
        }
        mav.addObject("monthlyPoints", monthlyPoints);

        return mav;
    }
}
