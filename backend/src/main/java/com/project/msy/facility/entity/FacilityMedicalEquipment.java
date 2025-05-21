package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 시설 의료 장비 정보 Entity
 */
@Entity
@Table(name = "facility_medical_equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityMedicalEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", nullable = false)
    private Facility facility;

    @Column(name = "equipment_name", nullable = false, length = 100)
    private String equipmentName;
}
