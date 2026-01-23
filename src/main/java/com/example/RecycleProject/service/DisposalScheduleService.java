package com.example.RecycleProject.service;

import com.example.RecycleProject.Repository.DisposalScheduleRepository;
import com.example.RecycleProject.domain.DisposalSchedule;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)


public class DisposalScheduleService {
    private final DisposalScheduleRepository disposalScheduleRepository;

    //특정 지역과 카테고리에 해당하는 배출 일정 조회
    @Transactional
    
     // 특정 지역의 모든 배출일정을 조회
    
    // 배출 일정 등록

}
