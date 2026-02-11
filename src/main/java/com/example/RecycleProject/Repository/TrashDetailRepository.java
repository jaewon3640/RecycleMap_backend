package com.example.RecycleProject.Repository;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.domain.TrashDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrashDetailRepository extends JpaRepository<TrashDetail, Long> {
    //조회 로직이 필요
    //1. 특정 지역, 특정 카테고리 조회를 통해서 배출 방법을 조회
    Optional<TrashDetail> findByRegionAndCategory(Region region, Category category);

    // 2. 품목 이름을 통해서 조회
    List<TrashDetail> findByItemNameContainingAndRegion(String itemName, Region region);

    // 3. 카테고리 이름을 사용한 조회
    List<TrashDetail> findAllByCategoryAndRegion(Category category, Region region);

}
