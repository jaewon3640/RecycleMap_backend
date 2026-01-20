package com.example.RecycleProject.controller;

import com.example.RecycleProject.DTO.RegionDTO;
import com.example.RecycleProject.service.RegionService;
import com.example.RecycleProject.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class RegionController {

    private final UserService userService;

    // 지역을 등록 시켜주는 메서드
    @PostMapping("/api/user/region")
    public ResponseEntity<String> updateRegion(
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RegionDTO regionDTO)
    {

        userService.updateMyRegion(userId, regionDTO);
        return ResponseEntity.ok("지역 저장 성공");
    }

}
