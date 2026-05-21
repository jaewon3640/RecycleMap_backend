package com.example.RecycleProject.Repository;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.domain.TrashDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrashDetailRepository extends JpaRepository<TrashDetail, Long> {

    Optional<TrashDetail> findByRegionAndCategory(Region region, Category category);

    // 품목명 검색은 결과 수가 많을 수 있어 페이징 적용
    Page<TrashDetail> findByItemNameContainingAndRegion(String itemName, Region region, Pageable pageable);

    // @Cacheable 메서드와 연동되므로 List 유지
    List<TrashDetail> findAllByCategoryAndRegion(Category category, Region region);
}
