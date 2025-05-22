package com.project.msy.admin.dashboard.service;

import com.project.msy.admin.dashboard.dto.*;
import com.project.msy.dashboard.dto.DailySaleDto;
import java.util.List;

public interface DashboardService {

     long getTotalFacilityCount();

    List<UserDailySignupDto> getDailyUserGrowth();

    List<DailySaleDto> getLast7DaysSales();

    List<PopularFacilityDto> getPopularFacilities();

    List<PopularProductDto>  getPopularProducts();

    List<FacilityTypeStatDto> getFacilityTypeStats();
}