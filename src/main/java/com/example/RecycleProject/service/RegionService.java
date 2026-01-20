package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.RegionDTO;
import com.example.RecycleProject.Repository.RegionRepository;
import com.example.RecycleProject.domain.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
