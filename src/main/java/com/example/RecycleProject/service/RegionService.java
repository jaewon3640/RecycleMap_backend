package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.RegionDTO;
import com.example.RecycleProject.Repository.RegionRepository;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.exception.RegionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    /*
      있으면 만들고 없으면 없으면 생성, RegionService는 지역 객체응 생성하거나
      찾아오는 역할만 하고 실제로 지역으로 지정하는것은 UserServuce에서 지정하여야 된다.
     */
    @Transactional
    public Region getOrCreateRegion(RegionDTO dto){
        return regionRepository.findByCityAndDistrictAndDong(dto.getCity(), dto.getDistrict(), dto.getDong())
                .orElseGet(() -> regionRepository.save(dto.toEntity()));
    }

    // 지역 전체 목록 (프론트가 하드코딩 대신 이걸로 로드 → region_id 단일 진실)
    public List<Region> findAll() {
        return regionRepository.findAll();
    }

    // PK 로 단건 조회 (지역 직접 선택 시 사용)
    public Region getById(Long regionId) {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new RegionNotFoundException("해당 지역이 없습니다. id=" + regionId));
    }
}
