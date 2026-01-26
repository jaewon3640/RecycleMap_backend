package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.TrashDetailDTO;
import com.example.RecycleProject.service.TrashDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

public class TrashDetailController {

    private final TrashDetailService trashDetailService;

    @GetMapping("/one-trash")
    public ResponseEntity<TrashDetailDTO.Response> getOneTrashDetail
            (@ModelAttribute @Valid TrashDetailDTO.Request dto) {

        TrashDetailDTO.Response result = trashDetailService.getDetailByRegionAndCategory(dto);

        return ResponseEntity.ok(result);
    }

    /*
        카테고리 부분이 없으니 DTO 세트로 줄 이유가 없다 아이디와 품목 이름만 던져주자
     */
    @GetMapping("/all-trash")
    public ResponseEntity<List<TrashDetailDTO.Response>> getAllTrash
            (@RequestParam String itemName,
             @RequestParam Long regionId) {

        List<TrashDetailDTO.Response> entityLists =
                trashDetailService.searchByItemName(itemName, regionId);

        return ResponseEntity.ok(entityLists);
    }
}
