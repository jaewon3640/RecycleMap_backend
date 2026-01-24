package com.example.RecycleProject.Repository;

import com.example.RecycleProject.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByCityAndDistrictAndDong(String city, String district, String dong);
}
