package com.project.msy.facility.entity;

import lombok.Getter;

enum FacilityType {
    SILVER_TOWN("실버타운"),
    NURSING_HOME("양로원"),
    CARE_HOME("요양원"),
    MEDICAL_CARE_HOSPITAL("요양병원"),
    DAY_CARE("주야간보호"),
    SHORT_TERM_CARE("단기보호"),
    VISITING_CARE("방문요양"),
    VISITING_NURSE("방문간호"),
    VISITING_BATH("방문목욕");

    @Getter
    private final String korean;
    FacilityType(String korean) { this.korean = korean; }

    @Override
    public String toString() {
        return korean;
    }
}
