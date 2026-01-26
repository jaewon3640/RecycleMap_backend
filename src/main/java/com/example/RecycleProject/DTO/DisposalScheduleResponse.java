package com.example.RecycleProject.DTO;

import com.example.RecycleProject.domain.DisposalSchedule;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DisposalScheduleResponse {
    private Long id;
    private String category;
    private String dayOfWeek;
    private String regionName;

    //.name()을 호출하면 Enum 상수의 이름(글자)을 그대로 가져온다. ENUM 내장 메서드

    public DisposalScheduleResponse(DisposalSchedule disposalSchedule) {
        this.id = disposalSchedule.getId();
        this.category = disposalSchedule.getCategory().name();
        this.dayOfWeek = disposalSchedule.getDisposal_day();
        if (disposalSchedule.getRegion() != null) {
            this.regionName = disposalSchedule.getRegion().getCity();
        }

    }
}
