package com.example.RecycleProject.DTO;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.domain.DisposalSchedule;
import com.example.RecycleProject.domain.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DisposalScheduleRequest {

    @NotNull(message = "지역정보는 필수 입니다.")
    private Long regionId;
    //사용자는 글자를 보나, 이제 실제로는 Region시에 id를 통해서 판별해야되므로
    //객체가 아닌 Region Id를 사용해야만 된다.

    @NotBlank(message = "카테고리는 필수 입니다.")
    private String category;

    @NotBlank(message = "배출요일을 입력해주세요")
    private String disposalDay;

    private String disposalTime;

    // 받을때 엔티티로 변환하자!
    // 근데 받는게 숫자인데...? 이럴때는 Service 계층에서 Region을 찾아서 주입해주자!
    public DisposalSchedule toEntity(Region region) {
        Category category1 = Category.valueOf(this.category.toUpperCase());

        return DisposalSchedule.createSchedule
                (region, category1, this.disposalDay, this.disposalTime);
    }

}
