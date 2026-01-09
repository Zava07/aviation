
package com.itu.aviation.controller;

import com.itu.aviation.entity.Billet;
import com.itu.aviation.service.BilletService;
import com.itu.aviation.service.ReservationService;
import com.itu.aviation.repository.BilletRepository;
import com.itu.aviation.repository.StatusBilletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;
 

@Controller
@RequestMapping("/billets")
public class BilletController {

    @Autowired
    private BilletService billetService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StatusBilletRepository statusBilletRepository;

    @Autowired
    private BilletRepository billetRepository;

    @GetMapping
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("billets/list");
        mav.addObject("pageTitle", "Billets");
        mav.addObject("currentPage", "billets");
        mav.addObject("billets", billetRepository.findAll());
        return mav;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("billets/list");
        mav.addObject("pageTitle", "Billets");
        mav.addObject("currentPage", "billets");
        mav.addObject("billets", billetService.findAll());
        mav.addObject("reservations", reservationService.findAll());
        mav.addObject("statusList", statusBilletRepository.findAll());
        mav.addObject("editBillet", billetService.findById(id));
        mav.addObject("openModal", "formModal");
        return mav;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Billet billet, RedirectAttributes redirect) {
        if (billet.getId() == null) {
            billetService.create(billet);
            redirect.addFlashAttribute("successMessage", "Billet créé");
        } else {
            billetService.update(billet.getId(), billet);
            redirect.addFlashAttribute("successMessage", "Billet mis à jour");
        }
        return "redirect:/billets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        billetService.delete(id);
        redirect.addFlashAttribute("successMessage", "Billet supprimé");
        return "redirect:/billets";
    }
}
