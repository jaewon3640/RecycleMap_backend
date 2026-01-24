package com.example.RecycleProject.Repository;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.domain.DisposalSchedule;
import com.example.RecycleProject.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisposalScheduleRepository extends JpaRepository<DisposalSchedule, Long> {
    // 특정 지역의 모든 배출일정을 조회
    List<DisposalSchedule> findByRegion(Region region);

    // 특정 지역 + 특정 카테고리의 일정만 조회
    Optional<DisposalSchedule> findByRegionAndCategory(Region region, Category category);
}
