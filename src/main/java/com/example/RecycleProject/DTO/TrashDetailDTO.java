package com.example.RecycleProject.DTO;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.domain.Region;
import com.example.RecycleProject.domain.TrashDetail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class TrashDetailDTO {
    @Data
    @NoArgsConstructor
    /*
        admin + 사용자 제보 수정을 위해서 RequestDTO가 필요함
        @NotBlank는 문자열 전용 따라서 ENUM에서는 NOTNULL 쓰기
     */
    public static class Request{
        @NotNull(message = "지역정보는 필수 입니다.")
        private Long regionId;

        @NotBlank(message = "카테고리를 입력해주세요")
        private String category;

        @NotBlank(message = "품목명을 입력해주세요")
        private String item_name;

        private String disposal_method; // 배출방법

        private String pre_treatment; // 전처리 방법

        private String caution; // 주의 사항

        public TrashDetail toEntity(Region region){

            // 카테고리를 Enum으로 변환
            // 이때 소문자로 반환되므로 UpperCase 적용해주기!
            Category enumCategory = Category.valueOf(this.category.toUpperCase());

            TrashDetail trashDetail = TrashDetail.createTrashDetail
                    (region, enumCategory, this.item_name, this.disposal_method, this.pre_treatment, this.caution);

            return trashDetail;
        }
    }

    /*
        수정이나 삭제 요청 고려 아이디 반환, 상세 페이지도 고려
     */
    @Data
    public static class Response{
        private Long id;
        private String itemName;
        private String category;
        private String method;
        private String treatment;
        private String caution;
        private String cityName;

        public Response(TrashDetail detail) {
            this.id = detail.getId();
            this.itemName = detail.getItemName();
            this.category = detail.getCategory().name();
            this.method = detail.getDisposal_method();
            this.treatment = detail.getPre_treatment();
            this.caution = detail.getCaution();
            this.cityName = detail.getRegion().getCity();
        }

    }

}
