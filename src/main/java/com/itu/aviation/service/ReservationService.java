package com.itu.aviation.service;

import com.itu.aviation.entity.*;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ReservationRepository;
import com.itu.aviation.repository.SiegeVolRepository;
import com.itu.aviation.repository.StatusSiegeVolRepository;
import com.itu.aviation.repository.StatutReservationRepository;
import com.itu.aviation.service.VolInstanceService;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final SiegeVolRepository siegeVolRepository;
    private final StatusSiegeVolRepository statusSiegeVolRepository;
    private final StatutReservationRepository statutReservationRepository;
    private final PrixVolClasseService prixVolClasseService;
    private final com.itu.aviation.service.CategoriePassagerService categoriePassagerService;
    private final VolInstanceService volInstanceService;

    @PersistenceContext
    private EntityManager entityManager;

    public ReservationService(ReservationRepository reservationRepository,
                              SiegeVolRepository siegeVolRepository,
                              StatusSiegeVolRepository statusSiegeVolRepository,
                              StatutReservationRepository statutReservationRepository,
                              PrixVolClasseService prixVolClasseService,
                              VolInstanceService volInstanceService,
                              com.itu.aviation.service.CategoriePassagerService categoriePassagerService) {
        this.reservationRepository = reservationRepository;
        this.siegeVolRepository = siegeVolRepository;
        this.statusSiegeVolRepository = statusSiegeVolRepository;
        this.statutReservationRepository = statutReservationRepository;
        this.prixVolClasseService = prixVolClasseService;
        this.volInstanceService = volInstanceService;
        this.categoriePassagerService = categoriePassagerService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
    }

    /**
     * Search reservations by optional criteria. Null/empty parameters are ignored.
     */
    public List<Reservation> search(String numero, String passagerName, String volNumero, Long statusId, LocalDate dateReservation) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> cq = cb.createQuery(Reservation.class);
        Root<Reservation> root = cq.from(Reservation.class);

        List<Predicate> preds = new ArrayList<>();

        if (numero != null && !numero.trim().isEmpty()) {
            preds.add(cb.like(cb.lower(root.get("numReservation")), "%" + numero.toLowerCase().trim() + "%"));
        }

        if (passagerName != null && !passagerName.trim().isEmpty()) {
            Join<Reservation, com.itu.aviation.entity.Passager> passager = root.join("passager", JoinType.LEFT);
            Predicate p1 = cb.like(cb.lower(passager.get("nom")), "%" + passagerName.toLowerCase().trim() + "%");
            Predicate p2 = cb.like(cb.lower(passager.get("prenom")), "%" + passagerName.toLowerCase().trim() + "%");
            preds.add(cb.or(p1, p2));
        }

        if (volNumero != null && !volNumero.trim().isEmpty()) {
            // Reservation.vol now references VolInstance; join then join to the route Vol to access numeroVol
            Join<Reservation, com.itu.aviation.entity.VolInstance> vin = root.join("vol", JoinType.LEFT);
            Join<com.itu.aviation.entity.VolInstance, com.itu.aviation.entity.Vol> route = vin.join("vol", JoinType.LEFT);
            preds.add(cb.like(cb.lower(route.get("numeroVol")), "%" + volNumero.toLowerCase().trim() + "%"));
        }

        if (statusId != null) {
            Join<Reservation, com.itu.aviation.entity.StatutReservation> st = root.join("statutReservation", JoinType.LEFT);
            preds.add(cb.equal(st.get("id"), statusId));
        }

        if (dateReservation != null) {
            LocalDateTime start = dateReservation.atStartOfDay();
            LocalDateTime end = dateReservation.atTime(LocalTime.MAX);
            preds.add(cb.between(root.get("dateReservation"), start, end));
        }

        cq.where(preds.toArray(new Predicate[0]));
        TypedQuery<Reservation> q = entityManager.createQuery(cq);
        return q.getResultList();
    }

    /**
     * Create reservation with business checks: if seat provided, ensure it's available and mark it reserved.
     */
    public Reservation create(Reservation r) {
        // ensure date
        if (r.getDateReservation() == null) {
            r.setDateReservation(LocalDateTime.now());
        }

        // compute price ONLY if not already set and class/vol present
        if ((r.getPrix() == null || r.getPrix().compareTo(BigDecimal.ZERO) == 0) 
                && r.getVol() != null && r.getClasse() != null) {
            r.setPrix(computePrice(r));
        }

        // Note: removed single-reservation-per-VolInstance restriction to allow multiple reservations per instance

        return reservationRepository.save(r);
    }

    public Reservation update(Long id, Reservation r) {
        if (!reservationRepository.existsById(id))
            throw new ResourceNotFoundException("Reservation not found: " + id);
        r.setId(id);
        return reservationRepository.save(r);
    }

    /** Cancel reservation instead of deleting: free the seat and mark status to annulé */
    public void cancelReservation(Long id) {
        Reservation r = findById(id);

        // free seat if present
        if (r.getSiege() != null && r.getVol() != null) {
            Long volId = r.getVol().getId();
            Long siegeId = r.getSiege().getId();
            SiegeVol sv = siegeVolRepository.findByVolInstance_IdAndSiege_Id(volId, siegeId)
                    .orElse(null);
            if (sv != null) {
                StatusSiegeVol free = statusSiegeVolRepository.findByStatusContainingIgnoreCase("libre")
                        .orElse(null);
                if (free != null) {
                    sv.setStatusSiegeVol(free);
                }
                sv.setPassager(null);
                siegeVolRepository.save(sv);
            }
        }

        // set reservation status to Annule
        Optional<StatutReservation> annule = statutReservationRepository.findByNomStatusContainingIgnoreCase("annul");
        StatutReservation srAnnule = annule.orElseGet(() -> statutReservationRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No reservation statuses configured")));
        r.setStatutReservation(srAnnule);
        reservationRepository.save(r);
    }

    /** Deletion is not allowed by business rules */
    public void delete(Long id) {
        throw new UnsupportedOperationException("Suppression des réservations interdite. Utilisez l'annulation.");
    }

    /**
     * Simple pricing algorithm using base price for vol+classe and adjustments for time/occupancy
     */
    private BigDecimal computePrice(Reservation r) {
        // base price from PrixVolClasse
        // base price from PrixVolClasse (prices stored against Vol route)
        com.itu.aviation.entity.VolInstance vi = r.getVol();
        if (vi != null && (vi.getVol() == null || vi.getVol().getId() == null) && vi.getId() != null) {
            try { vi = volInstanceService.findById(vi.getId()); } catch (Exception ex) { /* ignore */ }
        }

        Long routeVolId = (vi != null && vi.getVol() != null) ? vi.getVol().getId() : null;

        // Prefer price matching the passenger's category when available
        final Long desiredCatId = (r.getPassager() != null && r.getPassager().getCategoriePassager() != null)
            ? r.getPassager().getCategoriePassager().getId() : null;

        java.util.Optional<com.itu.aviation.entity.PrixVolClasse> p = prixVolClasseService.findAll().stream()
            .filter(x -> x.getVol() != null && routeVolId != null && x.getVol().getId().equals(routeVolId)
                && x.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId())
                && ((desiredCatId == null && x.getCategoriePassager() == null) || (x.getCategoriePassager() != null && desiredCatId != null && x.getCategoriePassager().getId().equals(desiredCatId))))
            .findFirst();

        BigDecimal base = BigDecimal.ZERO;
        if (p.isPresent()) {
            base = p.get().getPrix();
        } else {
            // fallback: any price for this vol+class (ignore category)
            java.util.Optional<com.itu.aviation.entity.PrixVolClasse> any = prixVolClasseService.findAll().stream()
                .filter(x -> x.getVol() != null && routeVolId != null && x.getVol().getId().equals(routeVolId)
                    && x.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId()))
                .findFirst();
            base = any.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(BigDecimal.ZERO);
        }

        // If passenger category has an explicit percentage reduction, apply it relative to the Adulte base price
        if (desiredCatId != null) {
            try {
                com.itu.aviation.entity.CategoriePassager desiredCat = categoriePassagerService.findById(desiredCatId);
                java.math.BigDecimal pct = desiredCat.getPourcentageReduction();
                if (pct != null) {
                    // find Adulte base price for same vol+classe
                    java.util.Optional<com.itu.aviation.entity.PrixVolClasse> adultePriceOpt = prixVolClasseService.findAll().stream()
                        .filter(x -> x.getVol() != null && routeVolId != null && x.getVol().getId().equals(routeVolId)
                            && x.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId())
                            && x.getCategoriePassager() != null && "Adulte".equalsIgnoreCase(x.getCategoriePassager().getCategorie()))
                        .findFirst();

                    java.math.BigDecimal adulteBase = adultePriceOpt.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(base);
                    if (adulteBase == null) adulteBase = java.math.BigDecimal.ZERO;
                    java.math.BigDecimal computed = adulteBase.multiply(pct).divide(new java.math.BigDecimal(100), 2, java.math.RoundingMode.HALF_UP);
                    return computed.setScale(2, java.math.RoundingMode.HALF_UP);
                }
            } catch (Exception ex) {
                // ignore and fallback to base
            }
        }

        // Return base price directly without dynamic adjustments
        return base.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Find a SiegeVol by volInstance id and siege id.
     */
    public Optional<SiegeVol> findSiegeVolByVolInstanceAndSiege(Long volInstanceId, Long siegeId) {
        if (volInstanceId == null || siegeId == null) return Optional.empty();
        return siegeVolRepository.findByVolInstance_IdAndSiege_Id(volInstanceId, siegeId);
    }

    /**
     * Return reservation (if any) for given volInstance id and siege id.
     * Useful to check if a seat is already reserved.
     */
    public Optional<Reservation> findReservationByVolInstanceAndSiege(Long volInstanceId, Long siegeId) {
        if (volInstanceId == null || siegeId == null) return Optional.empty();
        return reservationRepository.findByVol_IdAndSiege_Id(volInstanceId, siegeId);
    }
}