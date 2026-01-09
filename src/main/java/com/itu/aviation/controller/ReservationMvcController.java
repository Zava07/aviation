package com.itu.aviation.controller;

import com.itu.aviation.entity.Reservation;
import com.itu.aviation.service.ReservationService;
import com.itu.aviation.service.PassagerService;
import com.itu.aviation.service.VolService;
import com.itu.aviation.service.ClasseService;
import com.itu.aviation.repository.StatutReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
public class ReservationMvcController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PassagerService passagerService;

    @Autowired
    private VolService volService;

    @Autowired
    private ClasseService classeService;

    @Autowired
    private StatutReservationRepository statutReservationRepository;

    @Autowired
    private com.itu.aviation.service.SiegeVolService siegeVolService;

    @GetMapping
    public ModelAndView list(@RequestParam(required = false) String numero,
                             @RequestParam(required = false) String passager,
                             @RequestParam(required = false) String vol,
                             @RequestParam(required = false) Long status,
                             @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate dateReservation) {
        ModelAndView mav = new ModelAndView("reservations/list");

        // perform server-side search if any filter is provided
        boolean hasFilter = (numero != null && !numero.isBlank()) || (passager != null && !passager.isBlank()) ||
                (vol != null && !vol.isBlank()) || (status != null) || (dateReservation != null);

        if (hasFilter) {
            java.time.LocalDate d = null;
            if (dateReservation != null) d = dateReservation;
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

    @PostMapping("/save")
    public String save(@ModelAttribute Reservation reservation, RedirectAttributes redirect) {
        try {
            if (reservation.getId() == null) {
                reservationService.create(reservation);
                redirect.addFlashAttribute("successMessage", "Réservation créée");
            } else {
                reservationService.update(reservation.getId(), reservation);
                redirect.addFlashAttribute("successMessage", "Réservation mise à jour");
            }
        } catch (IllegalStateException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirect.addFlashAttribute("errorMessage", "Erreur lors de la création de la réservation: " + ex.getMessage());
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
}
