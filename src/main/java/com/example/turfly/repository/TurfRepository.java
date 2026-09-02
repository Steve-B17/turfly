package com.example.turfly.repository;

import com.example.turfly.entity.Turf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurfRepository extends JpaRepository<Turf, Long> {
    List<Turf> findByOwnerId(Long ownerId);
    Page<Turf> findAll(Pageable pageable);
}