package com.project.msy.facility.service;

import com.project.msy.S3.S3Uploader;
import com.project.msy.facility.dto.FacilityCreateRequestDTO;
import com.project.msy.facility.dto.FacilityResponseDTO;
import com.project.msy.facility.entity.Facility;
import com.project.msy.facility.entity.FacilityImage;
import com.project.msy.facility.repository.FacilityImageRepository;
import com.project.msy.facility.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final FacilityImageRepository facilityImageRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Facility createFacility(FacilityCreateRequestDTO dto, List<MultipartFile> imageFiles) throws IOException {
        Facility facility = Facility.builder()
                .type(dto.getType())
                .name(dto.getName())
                .establishedYear(dto.getEstablishedYear())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .homepage(dto.getHomepage())
                .grade(dto.getGrade())
                .description(dto.getDescription())
                .weekdayHours(dto.getWeekdayHours())
                .weekendHours(dto.getWeekendHours())
                .holidayHours(dto.getHolidayHours())
                .visitingHours(dto.getVisitingHours())
                .facilitySize(dto.getFacilitySize())
                .build();

        Facility savedFacility = facilityRepository.save(facility);

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<FacilityImage> images = imageFiles.stream()
                    .map(file -> {
                        try {
                            String url = s3Uploader.upload(file, "facilities");
                            return FacilityImage.builder()
                                    .facility(savedFacility)
                                    .imageUrl(url)
                                    .build();
                        } catch (IOException e) {
                            throw new RuntimeException("S3 업로드 실패", e);
                        }
                    })
                    .collect(Collectors.toList());
            facilityImageRepository.saveAll(images);
        }

        return savedFacility;
    }

    public List<FacilityResponseDTO> getAllFacilities() {
        return facilityRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityResponseDTO> getFacilitiesByType(String type) {
        return facilityRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<FacilityResponseDTO> searchFacilities(String type, String grade, String facilitySize, String sort, String region) {
        List<Facility> facilities;

        if (region != null && !region.isEmpty()) {
            String[] parts = region.trim().split(" ");
            facilities = facilityRepository.findAll().stream()
                    .filter(f -> {
                        String addr = f.getAddress();
                        return Arrays.stream(parts).allMatch(p -> {
                            String norm = p.replaceAll("(시|군|구|도)$", "");
                            return addr.contains(p) || addr.contains(norm);
                        });
                    })
                    .collect(Collectors.toList());
        } else {
            facilities = facilityRepository.findAll();
        }

        if (type != null) {
            facilities = facilities.stream()
                    .filter(f -> f.getType().equals(type))
                    .collect(Collectors.toList());
        }

        if (grade != null && !grade.equals("등급제외")) {
            facilities = facilities.stream()
                    .filter(f -> f.getGrade() != null && f.getGrade().equalsIgnoreCase(grade))
                    .collect(Collectors.toList());
        }

        if (facilitySize != null) {
            facilities = facilities.stream()
                    .filter(f -> f.getFacilitySize() != null && f.getFacilitySize().equals(facilitySize))
                    .collect(Collectors.toList());
        }

        if (sort != null) {

            switch (sort) {
                case "view":
                    facilities.sort(Comparator.comparing(Facility::getViewCount, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                case "like":
                    facilities.sort(Comparator.comparing(Facility::getLikeCount, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
                    break;
                default:
                    break;
            }
        }


        return facilities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private FacilityResponseDTO convertToDTO(Facility facility) {
        List<String> imageUrls = facility.getFacilityImages().stream()
                .map(FacilityImage::getImageUrl)
                .collect(Collectors.toList());

        return FacilityResponseDTO.builder()
                .id(facility.getId())
                .name(facility.getName())
                .type(facility.getType())
                .establishedYear(facility.getEstablishedYear())
                .address(facility.getAddress())
                .phone(facility.getPhone())
                .homepage(facility.getHomepage())
                .grade(facility.getGrade())
                .description(facility.getDescription())
                .weekdayHours(facility.getWeekdayHours())
                .weekendHours(facility.getWeekendHours())
                .holidayHours(facility.getHolidayHours())
                .visitingHours(facility.getVisitingHours())
                .facilitySize(facility.getFacilitySize())
                .createdAt(facility.getCreatedAt())
                .viewCount(facility.getViewCount())
                .likeCount(facility.getLikeCount())
                .imageUrls(imageUrls)
                .build();
    }

    @Transactional(readOnly = true)
    public FacilityResponseDTO getFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 존재하지 않습니다. id=" + id));
        return convertToDTO(facility);
    }

    @Transactional
    public FacilityResponseDTO updateFacility(Long id, FacilityCreateRequestDTO dto, List<MultipartFile> imageFiles) throws IOException {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 존재하지 않습니다. id=" + id));

        facility.setType(dto.getType());
        facility.setName(dto.getName());
        facility.setEstablishedYear(dto.getEstablishedYear());
        facility.setAddress(dto.getAddress());
        facility.setPhone(dto.getPhone());
        facility.setHomepage(dto.getHomepage());
        facility.setGrade(dto.getGrade());
        facility.setDescription(dto.getDescription());
        facility.setWeekdayHours(dto.getWeekdayHours());
        facility.setWeekendHours(dto.getWeekendHours());
        facility.setHolidayHours(dto.getHolidayHours());
        facility.setVisitingHours(dto.getVisitingHours());
        facility.setFacilitySize(dto.getFacilitySize());

        facilityImageRepository.deleteAll(facility.getFacilityImages());
        facility.getFacilityImages().clear();

        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<FacilityImage> images = imageFiles.stream()
                    .map(file -> {
                        try {
                            String url = s3Uploader.upload(file, "facilities");
                            return FacilityImage.builder()
                                    .facility(facility)
                                    .imageUrl(url)
                                    .build();
                        } catch (IOException e) {
                            throw new RuntimeException("S3 업로드 실패", e);
                        }
                    })
                    .collect(Collectors.toList());

            facility.getFacilityImages().addAll(images);
            facilityImageRepository.saveAll(images);
        }

        return convertToDTO(facility);
    }

    @Transactional
    public void deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 존재하지 않습니다. id=" + id));
        facilityRepository.delete(facility);
    }

    @Transactional
    public void increaseViewCount(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 시설이 존재하지 않습니다. id=" + id));
        int current = Optional.ofNullable(facility.getViewCount()).orElse(0);
        facility.setViewCount(current + 1);
        facilityRepository.save(facility);
    }
}
