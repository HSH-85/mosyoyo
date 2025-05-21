package com.project.msy.facility.entity;

import com.project.msy.facility.entity.Facility;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private LocalDateTime createdAt;
}