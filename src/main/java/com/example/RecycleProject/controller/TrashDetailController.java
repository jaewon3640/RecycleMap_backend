package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.TrashDetailDTO;
import com.example.RecycleProject.service.TrashDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/*
    1. 특정 지역 과 카테고리로 배출요령을 조회
    2. 품목 이름을 통해서 조회, 이때 지역의 대한 배출요령을 출시해야 된다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trash-detail")
@Slf4j
public class TrashDetailController {

    private final TrashDetailService trashDetailService;

    @GetMapping("/one-trash")
    public ResponseEntity<TrashDetailDTO.Response> getOneTrashDetail
            (@ModelAttribute @Valid TrashDetailDTO.Request dto) {

        log.info("배출정보 단건 조회  RegionId : {}, Category : {}", dto.getRegionId(), dto.getCategory());
        TrashDetailDTO.Response result = trashDetailService.getDetailByRegionAndCategory(dto);

        log.debug("조회된 베출 정보 Result : {}", result);

        return ResponseEntity.ok(result);
    }

    /*
        카테고리 부분이 없으니 DTO 세트로 줄 이유가 없다 아이디와 품목 이름만 던져주자
     */
    @GetMapping("/all-trash")
    public ResponseEntity<List<TrashDetailDTO.Response>> getAllTrash
            (@RequestParam String itemName,
             @RequestParam Long regionId) {

        log.info("배출 정보 모두 조회  itemName : {}, RegionId : {}", itemName, regionId);
        List<TrashDetailDTO.Response> entityLists =
                trashDetailService.searchByItemName(itemName, regionId);

        if (entityLists.isEmpty()) {
            log.warn("[SearchEmpty] 검색 결과가 없습니다. - ItemName: {}, RegionId: {}", itemName, regionId);
        } else {
            log.info("[SearchSuccess] 검색 완료 - 검색된 건수: {}건", entityLists.size());
            log.debug("[Result] 검색 결과 리스트: {}", entityLists);
        }

        return ResponseEntity.ok(entityLists);
    }
}
