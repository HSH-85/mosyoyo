package com.project.msy.facility.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacilitySpecializationId implements java.io.Serializable {
    private Long facilityId;
    private Long specializationId;

    // equals(), hashCode() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacilitySpecializationId)) return false;
        FacilitySpecializationId that = (FacilitySpecializationId) o;
        return facilityId.equals(that.facilityId) && specializationId.equals(that.specializationId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(facilityId, specializationId);
    }
}
