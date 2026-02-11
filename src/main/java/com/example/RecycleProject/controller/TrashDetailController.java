package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.TrashDetailDTO;
import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.service.TrashDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
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
        품목 명으로 조회
     */
    @GetMapping("/name")
    public ResponseEntity<List<TrashDetailDTO.Response>> getByItemName
            (@RequestParam String itemName,
             @RequestParam Long regionId) {

        List<TrashDetailDTO.Response> entityLists =
                trashDetailService.searchByItemName(itemName, regionId);

        return ResponseEntity.ok(entityLists);
    }

    /*
        카테고리를 통해서 모든 배출 요령을 조회
     */
    @GetMapping("/all-trash")
    public ResponseEntity<List<TrashDetailDTO.Response>> getAllByCategory(
            @RequestParam Long regionId,
            @RequestParam Category category) {
        List<TrashDetailDTO.Response> result =
                trashDetailService.getAllDetailByCategory(category, regionId);

        return ResponseEntity.ok(result);
    }
}
