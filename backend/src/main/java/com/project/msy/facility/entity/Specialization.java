package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 특화 영역 마스터 Entity
 */
@Entity
@Table(name = "specializations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specializations_name", nullable = false, length = 100)
    private String name;

    @ManyToMany(mappedBy = "specializations")
    private Set<Facility> facilities = new HashSet<>();
}