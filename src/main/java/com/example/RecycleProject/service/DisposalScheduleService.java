package com.example.RecycleProject.service;

import com.example.RecycleProject.DTO.DisposalScheduleRequest;
import com.example.RecycleProject.DTO.DisposalScheduleResponse;
import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.Repository.DisposalScheduleRepository;
import com.example.RecycleProject.Repository.RegionRepository;
import com.example.RecycleProject.domain.DisposalSchedule;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.exception.RegionNotFoundException;
import com.example.RecycleProject.exception.ScheduleException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)


public class DisposalScheduleService {
    private final DisposalScheduleRepository disposalScheduleRepository;

    private final RegionRepository regionRepository;

    //특정 지역과 카테고리에 해당하는 배출 일정 조회
    /*
        조회 에서는 엔티티 변환이 딱히 필요 없음 트랜젝션이 일어나지 않기 때문에
     */
    public DisposalScheduleResponse getDisposalSchedule(DisposalScheduleRequest disposalDTO) {
        log.info("[일정 단건 조회 시작] RegionId: {}, Category: {}", disposalDTO.getRegionId(), disposalDTO.getCategory());

        Region region = regionRepository.findById(disposalDTO.getRegionId())
                .orElseThrow(() -> new RegionNotFoundException("존재하지 않는 지역입니다."));


        try {
            Category category = Category.valueOf(disposalDTO.getCategory().toUpperCase());
            Optional<DisposalSchedule> entityList = disposalScheduleRepository.findByRegionAndCategory(region, category);

            if (entityList.isPresent()) {
                log.info("[일정 단건 조회 성공] Schedule ID: {}", entityList.get().getId());
                return new DisposalScheduleResponse(entityList.get());
            } else {
                log.warn("[ScheduleNotFound] 일정 정보 없음 - Region: {}, Category: {}", region.getCity(), category);
                throw new ScheduleException("존재하지 않는 일정 입니다");
            }
        }
        catch (IllegalArgumentException e) {
            log.error("[InvalidCategory] 잘못된 카테고리 값: {}", disposalDTO.getCategory());
            throw e;
        }


    }


    // 특정 지역의 모든 배출일정을 조회
    /*
        이떄 지역으만 조회를 할수 있으니 파라미터는 DTO가 아닌 단순하게 하는것을 권장한다.
        DTO로 해도는 되는데 카테고리등의 모든 값을 채워서 보내니 컨트롤러가 복잡해짐
     */
    public List<DisposalScheduleResponse> getAllDisposalSchedule(Long regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RegionNotFoundException("존재하지 않는 지역입니다."));


        List<DisposalSchedule> byRegion = disposalScheduleRepository.findByRegion(region);

        List<DisposalScheduleResponse> dtoList = new ArrayList<>();

        for (DisposalSchedule schedule : byRegion) {
            DisposalScheduleResponse disposalScheduleResponse = new DisposalScheduleResponse(schedule);
            dtoList.add(disposalScheduleResponse);
        }

        return dtoList;
    }

    /*
        배출 일정 등록
        1. 해당 지역이 실제로 존재하는지 확인하면서 객체를 가져오기
        2, DTO를 바탕으로 엔티티 생성
        3. DB에 저장후에 id를 반환한다.
        id반환 이유는? 상세페이지로 이동을 시키거나, 등록되었다는 문자를 날리기 위함
     */
    @Transactional
    public Long saveSchedule(DisposalScheduleRequest dto) {
        Region region = regionRepository.findById(dto.getRegionId())
                .orElseThrow(() -> new RegionNotFoundException("존재하지 않는 지역입니다."));

        Category category = Category.valueOf(dto.getCategory().toUpperCase());

        if (disposalScheduleRepository.existsByRegionAndCategory(region,category)) {
            throw new ScheduleException("이미 등록된 일정입니다.");
        }

        DisposalSchedule schedule = dto.toEntity(region);

        return disposalScheduleRepository.save(schedule).getId();
    }

}
