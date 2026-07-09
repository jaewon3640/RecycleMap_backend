package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.Region;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 빌더를 위한 생성자 추가
public class RegionDTO {

    // 지역 직접 선택 시 PK. 있으면 이 id 로 바로 링크(중복 region 생성 방지).
    private Long regionId;

    @NotBlank(message = "지역 정보를 입력해주세요.")
    private String city;

    @NotBlank(message = "지역 정보를 입력해주세요.")
    private String district;

    private String dong;

    public Region toEntity(){
        return Region.createRegion(this.city, this.district, this.dong);
    }

    // 지역 목록 조회 응답 DTO
    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String city;
        private String district;
        private String dong;

        public static Response from(Region region) {
            return Response.builder()
                    .id(region.getId())
                    .city(region.getCity())
                    .district(region.getDistrict())
                    .dong(region.getDong())
                    .build();
        }
    }
}
