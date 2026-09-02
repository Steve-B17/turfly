package com.example.turfly.service;

import com.example.turfly.dto.TurfRequest;
import com.example.turfly.dto.TurfResponse;
import com.example.turfly.entity.Turf;
import com.example.turfly.entity.User;
import com.example.turfly.repository.TurfRepository;
import com.example.turfly.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurfService {

    private final TurfRepository turfRepository;
    private final UserRepository userRepository;

    public TurfService(TurfRepository turfRepository, UserRepository userRepository) {
        this.turfRepository = turfRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TurfResponse createTurf(TurfRequest request, String ownerEmail) {
        // unchanged
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Turf turf = new Turf();
        turf.setName(request.getName());
        turf.setAddress(request.getAddress());
        turf.setSportType(request.getSportType());
        turf.setPrice(request.getPrice());
        turf.setOwner(owner);

        Turf saved = turfRepository.save(turf);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TurfResponse> getAllTurfs() {
        return turfRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TurfResponse> getMyTurfs(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        return turfRepository.findByOwnerId(owner.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deleteTurf(Long turfId, String requesterEmail) {
        Turf turf = turfRepository.findById(turfId)
                .orElseThrow(() -> new IllegalArgumentException("Turf not found"));

        if (!turf.getOwner().getEmail().equals(requesterEmail)) {
            throw new AccessDeniedException("You do not own this turf");
        }

        turfRepository.delete(turf);
    }

    private TurfResponse toResponse(Turf turf) {
        return new TurfResponse(
                turf.getId(),
                turf.getName(),
                turf.getAddress(),
                turf.getSportType(),
                turf.getPrice(),
                turf.getOwner().getName()
        );
    }
}