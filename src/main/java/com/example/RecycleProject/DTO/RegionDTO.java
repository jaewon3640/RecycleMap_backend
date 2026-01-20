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

    @NotBlank(message = "지역 정보를 입력해주세요.")
    private String city;

    @NotBlank(message = "지역 정보를 입력해주세요.")
    private String district;

    private String dong;

    public Region toEntity(){
        return Region.createRegion(this.city, this.district, this.dong);
    }


}
