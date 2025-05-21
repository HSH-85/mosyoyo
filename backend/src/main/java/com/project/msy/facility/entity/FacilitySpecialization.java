package com.project.msy.facility.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "facility_specializations")
@IdClass(FacilitySpecializationId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilitySpecialization {
    @Id
    @Column(name = "f_id")
    private Long facilityId;

    @Id
    @Column(name = "s_id")
    private Long specializationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_id", insertable = false, updatable = false)
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "s_id", insertable = false, updatable = false)
    private Specialization specialization;
}