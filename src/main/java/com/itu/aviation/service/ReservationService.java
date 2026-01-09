package com.itu.aviation.service;

import com.itu.aviation.entity.*;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.ReservationRepository;
import com.itu.aviation.repository.SiegeVolRepository;
import com.itu.aviation.repository.StatusSiegeVolRepository;
import com.itu.aviation.repository.StatutReservationRepository;
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

    @PersistenceContext
    private EntityManager entityManager;

    public ReservationService(ReservationRepository reservationRepository,
                              SiegeVolRepository siegeVolRepository,
                              StatusSiegeVolRepository statusSiegeVolRepository,
                              StatutReservationRepository statutReservationRepository,
                              PrixVolClasseService prixVolClasseService) {
        this.reservationRepository = reservationRepository;
        this.siegeVolRepository = siegeVolRepository;
        this.statusSiegeVolRepository = statusSiegeVolRepository;
        this.statutReservationRepository = statutReservationRepository;
        this.prixVolClasseService = prixVolClasseService;
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
            Join<Reservation, com.itu.aviation.entity.Vol> vol = root.join("vol", JoinType.LEFT);
            preds.add(cb.like(cb.lower(vol.get("numeroVol")), "%" + volNumero.toLowerCase().trim() + "%"));
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

        // compute price if class/vol present
        if (r.getVol() != null && r.getClasse() != null) {
            r.setPrix(computePrice(r));
        }

        // if seat assigned, verify availability and reserve it
        if (r.getSiege() != null && r.getVol() != null) {
            Long volId = r.getVol().getId();
            Long siegeId = r.getSiege().getId();
            SiegeVol sv = siegeVolRepository.findByVol_IdAndSiege_Id(volId, siegeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found on this flight"));

            String current = sv.getStatusSiegeVol() != null ? sv.getStatusSiegeVol().getStatus() : "";
            if (!current.equalsIgnoreCase("Libre") && !current.equalsIgnoreCase("Free")) {
                throw new IllegalStateException("Seat is not available");
            }

            // find reserve status
            StatusSiegeVol reserveStatus = statusSiegeVolRepository.findByStatusContainingIgnoreCase("reserv")
                    .orElseThrow(() -> new ResourceNotFoundException("Reserve status not configured"));

            sv.setStatusSiegeVol(reserveStatus);
            sv.setPassager(r.getPassager());
            siegeVolRepository.save(sv);

            // set reservation status to En attente by default (fallback to first configured status if not found)
            Optional<StatutReservation> enAttente = statutReservationRepository.findByNomStatusContainingIgnoreCase("en attente");
            StatutReservation sr = enAttente.orElseGet(() -> statutReservationRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No reservation statuses configured")));
            r.setStatutReservation(sr);
        }

        // final save
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
            SiegeVol sv = siegeVolRepository.findByVol_IdAndSiege_Id(volId, siegeId)
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
        java.util.Optional<com.itu.aviation.entity.PrixVolClasse> p = prixVolClasseService.findAll().stream()
                .filter(x -> x.getVol() != null && x.getVol().getId().equals(r.getVol().getId())
                        && x.getClasse() != null && x.getClasse().getId().equals(r.getClasse().getId()))
                .findFirst();

        BigDecimal base = p.map(com.itu.aviation.entity.PrixVolClasse::getPrix).orElse(BigDecimal.ZERO);

        // time factor
        BigDecimal timeFactor = BigDecimal.ZERO;
        if (r.getVol() != null && r.getVol().getDateDepart() != null) {
            long days = Math.max(0, Duration.between(LocalDateTime.now(), r.getVol().getDateDepart()).toDays());
            if (days < 7) timeFactor = new BigDecimal("0.20");
            else if (days < 30) timeFactor = new BigDecimal("0.10");
        }

        // occupancy factor
        BigDecimal occupancyFactor = BigDecimal.ZERO;
        if (r.getVol() != null) {
            java.util.List<SiegeVol> all = siegeVolRepository.findByVol_Id(r.getVol().getId());
            long total = all.size();
            long occupied = all.stream().filter(sv -> {
                String st = sv.getStatusSiegeVol() != null ? sv.getStatusSiegeVol().getStatus() : "";
                return !st.equalsIgnoreCase("Libre") && !st.equalsIgnoreCase("Free");
            }).count();
            if (total > 0) {
                double rate = (double) occupied / (double) total;
                if (rate > 0.8) occupancyFactor = new BigDecimal("0.25");
                else if (rate > 0.6) occupancyFactor = new BigDecimal("0.10");
            }
        }

        BigDecimal multiplier = BigDecimal.ONE.add(timeFactor).add(occupancyFactor);
        return base.multiply(multiplier).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}