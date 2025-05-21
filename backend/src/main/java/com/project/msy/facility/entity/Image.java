package com.project.msy.facility.entity;

import com.project.msy.facility.entity.Facility;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String description;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;
}