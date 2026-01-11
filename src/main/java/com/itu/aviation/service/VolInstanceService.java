package com.itu.aviation.service;

import com.itu.aviation.entity.VolInstance;
import com.itu.aviation.exception.ResourceNotFoundException;
import com.itu.aviation.repository.VolInstanceRepository;
import com.itu.aviation.repository.SiegeRepository;
import com.itu.aviation.repository.SiegeVolRepository;
import com.itu.aviation.repository.StatusSiegeVolRepository;
import com.itu.aviation.entity.SiegeVol;
import com.itu.aviation.entity.SiegeVolId;
import com.itu.aviation.entity.Siege;
import com.itu.aviation.entity.StatusSiegeVol;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolInstanceService {

    private final VolInstanceRepository volInstanceRepository;
    private final SiegeRepository siegeRepository;
    private final SiegeVolRepository siegeVolRepository;
    private final StatusSiegeVolRepository statusSiegeVolRepository;

    public VolInstanceService(VolInstanceRepository volInstanceRepository,
                              SiegeRepository siegeRepository,
                              SiegeVolRepository siegeVolRepository,
                              StatusSiegeVolRepository statusSiegeVolRepository) {
        this.volInstanceRepository = volInstanceRepository;
        this.siegeRepository = siegeRepository;
        this.siegeVolRepository = siegeVolRepository;
        this.statusSiegeVolRepository = statusSiegeVolRepository;
    }

    public List<VolInstance> findAll() {
        return volInstanceRepository.findAll();
    }

    public VolInstance findById(Long id) {
        return volInstanceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vol instance not found: " + id));
    }

    public VolInstance create(VolInstance vi) {
        // business rule: an airplane cannot be assigned to overlapping instances
        if (vi.getAvion() != null && vi.getAvion().getId() != null) {
            Long avionId = vi.getAvion().getId();
            List<VolInstance> overlaps = volInstanceRepository.findOverlapping(avionId, vi.getDateDepart(), vi.getDateArrivee());
            if (overlaps != null && !overlaps.isEmpty()) {
                throw new IllegalStateException("L'avion est déjà affecté à une autre instance de vol pendant cette période.");
            }
        }
        VolInstance saved = volInstanceRepository.save(vi);

        // initialize SiegeVol entries for this instance based on Avion's sieges
        if (saved.getAvion() != null && saved.getAvion().getId() != null) {
            List<Siege> sieges = siegeRepository.findAll().stream().filter(s -> s.getAvion() != null && s.getAvion().getId().equals(saved.getAvion().getId())).toList();
            // find default free status
            StatusSiegeVol freeStatus = statusSiegeVolRepository.findByStatusContainingIgnoreCase("libre").orElse(null);
            for (Siege s : sieges) {
                SiegeVolId id = new SiegeVolId(saved.getId(), s.getId());
                boolean exists = siegeVolRepository.findByVolInstance_IdAndSiege_Id(saved.getId(), s.getId()).isPresent();
                if (!exists) {
                    SiegeVol sv = new SiegeVol();
                    sv.setId(id);
                    sv.setVolInstance(saved);
                    sv.setSiege(s);
                    if (freeStatus != null) sv.setStatusSiegeVol(freeStatus);
                    siegeVolRepository.save(sv);
                }
            }
        }

        return saved;
    }

    public VolInstance update(Long id, VolInstance vi) {
        if (!volInstanceRepository.existsById(id))
            throw new ResourceNotFoundException("Vol instance not found: " + id);
        vi.setId(id);
        if (vi.getAvion() != null && vi.getAvion().getId() != null) {
            Long avionId = vi.getAvion().getId();
            List<VolInstance> overlaps = volInstanceRepository.findOverlapping(avionId, vi.getDateDepart(), vi.getDateArrivee());
            overlaps.removeIf(v -> v.getId().equals(id));
            if (overlaps != null && !overlaps.isEmpty()) {
                throw new IllegalStateException("L'avion est déjà affecté à une autre instance de vol pendant cette période.");
            }
        }
        return volInstanceRepository.save(vi);
    }

    /**
     * Return list of VolInstance for given avion id and vol id
     */
    public List<VolInstance> findByAvionAndVol(Long avionId, Long volId) {
        if (avionId == null && volId == null) return List.of();
        return volInstanceRepository.findByAvion_IdAndVol_Id(avionId, volId);
    }

    public void delete(Long id) {
        volInstanceRepository.deleteById(id);
    }

    /**
     * Initialize missing SiegeVol rows for all existing VolInstances.
     * Returns the number of created siege_vol rows.
     */
    public int initializeMissingSiegeVolsForAll() {
        int created = 0;
        List<VolInstance> all = volInstanceRepository.findAll();
        StatusSiegeVol freeStatus = statusSiegeVolRepository.findByStatusContainingIgnoreCase("libre").orElse(null);
        for (VolInstance vi : all) {
            if (vi.getAvion() == null || vi.getAvion().getId() == null) continue;
            List<Siege> sieges = siegeRepository.findAll().stream().filter(s -> s.getAvion() != null && s.getAvion().getId().equals(vi.getAvion().getId())).toList();
            for (Siege s : sieges) {
                boolean exists = siegeVolRepository.findByVolInstance_IdAndSiege_Id(vi.getId(), s.getId()).isPresent();
                if (!exists) {
                    SiegeVol sv = new SiegeVol();
                    sv.setId(new SiegeVolId(vi.getId(), s.getId()));
                    sv.setVolInstance(vi);
                    sv.setSiege(s);
                    if (freeStatus != null) sv.setStatusSiegeVol(freeStatus);
                    siegeVolRepository.save(sv);
                    created++;
                }
            }
        }
        return created;
    }

   
}
