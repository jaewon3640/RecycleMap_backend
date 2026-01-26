package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.TrashDetailDTO;
import com.example.RecycleProject.Repository.RegionRepository;
import com.example.RecycleProject.Repository.TrashDetailRepository;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.domain.TrashDetail;
import com.example.RecycleProject.exception.RegionNotFoundException;
import com.example.RecycleProject.exception.TrashDetailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    1. 특정 지역 과 카테고리로 배출요령을 조회
    2. 품목 이름을 통해서 조회
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrashDetailService {

    private final TrashDetailRepository trashDetailRepository;
    private final RegionRepository regionRepository;

    /*
        1. 특정 지역과 카테고리로 배출요령 엔티티를 조회한다.
        dto 반환은 컨트롤러에서 하는것이 아닌 서비스에서 반환하자

     */
    public TrashDetailDTO.Response getDetailByRegionAndCategory(TrashDetailDTO.Request dto) {
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new RegionNotFoundException("존재하지 않는 지역입니다."));

        Optional<TrashDetail> byRegionAndCategory = trashDetailRepository.findByRegionAndCategory(region, dto.getCategory());

        if (byRegionAndCategory.isPresent()) {
            TrashDetail trashDetail = byRegionAndCategory.get();
            return new TrashDetailDTO.Response(trashDetail);
        }else{
            throw new TrashDetailNotFoundException("해당 카테고리의 배출 정보를 찾을수 없습니다.");
        }
    }

        /*
            2. 품목 이름을 이용해서 전체 조회
         */
    public List<TrashDetailDTO.Response> searchByItemName(String itemName, Long regionId){
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RegionNotFoundException("존재하지 않는 지역입니다."));

        List<TrashDetail> entityLit
                = trashDetailRepository.findByItemNameContainingAndRegion(itemName, region);

        //결과를 담을 DTO 그릇을 준비
        List<TrashDetailDTO.Response> dtoList = new ArrayList<>();

        for (TrashDetail trashDetail : entityLit) {
            TrashDetailDTO.Response response = new TrashDetailDTO.Response(trashDetail);

            dtoList.add(response);
        }

        /*
        return trashDetailRepository.findByItemNameContainingAndRegion(itemName, region)
            .stream()
            .map(TrashDetailDTO.Response::new)
            .toList(); stream 표현
         */

        return dtoList;
    }
}
