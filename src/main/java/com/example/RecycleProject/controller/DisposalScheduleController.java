package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.DisposalScheduleRequest;
import com.example.RecycleProject.DTO.DisposalScheduleResponse;
import com.example.RecycleProject.domain.DisposalSchedule;
import com.example.RecycleProject.service.DisposalScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")

public class DisposalScheduleController {

    private final DisposalScheduleService disposalScheduleService;

    /*
     특정 지역과 카테고리에 해당하는 배출 일정 조회
    근데 문제는? 나는 DTO로 받아서 날라야 된다! 지역과 카테고리를 모두 봐야되니!
     @ModelAttribute는 JSON에서는 사용 불가, 메세지 body에 오므로
        1.) postMapping을 통해서 JSON으로 프론트에서 body로 보내면 body로 받는다
        2.) get을 이용하돼 쿼리 파라미터 바인딩 URL 뒤에 값을 보낼수 있도록 한다
        -> http 법칙을 지키는게 더 좋을꺼 같으니 get을 쓰자

        컨트롤러 계층은 최대한 얇게 만들어야 되므로, 요청 받기, 서비스 호출, 응답 내려주기

     */
    @GetMapping("/disposalOne")
    public ResponseEntity<DisposalScheduleResponse> getOne
    (@ModelAttribute DisposalScheduleRequest dto) {

        log.info("[GET] 일정 단건 조회 요청 - RegionId: {}, Category: {}", dto.getRegionId(), dto.getCategory());

        DisposalScheduleResponse schedule = disposalScheduleService.getDisposalSchedule(dto);

        log.debug("조회된 일정 데이터: {}", schedule);

        return ResponseEntity.ok(schedule);
    }

    // 특정 지역의 모든 일정을 조회
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<DisposalScheduleResponse>> getAll
    (@PathVariable("regionId") Long regionId){

        log.info("지역별 전체 일정 조회 요청 -Region : {}", regionId);
        List<DisposalScheduleResponse> allDisposalSchedule = disposalScheduleService.getAllDisposalSchedule(regionId);

        log.info("지약뱔 전체 일정 조회 완료 건수 : {}", allDisposalSchedule.size());
        return ResponseEntity.ok(allDisposalSchedule);
    }

    /*
        배출일정 등록
     */

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody @Valid DisposalScheduleRequest disposalScheduleRequest){

        log.info("신규 일정 등록 요청 시작");
        log.debug("등록 요청 상세 데이터 : {}", disposalScheduleRequest);

        Long id = disposalScheduleService.saveSchedule(disposalScheduleRequest);

        log.info("신규 일정 등록 성공 -생성된 ID : {}", id);

        return ResponseEntity.ok(id);
    }

}
