package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.Category;
import com.example.RecycleProject.exception.RegionNotFoundException;
import com.example.RecycleProject.exception.ScheduleException;
import jakarta.persistence.*;
import lombok.Getter;

@Table(
        name = "disposal_schedule",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_region_category", // 제약 조건 이름
                        columnNames = {"region_id", "category"} // 이 두 컬럼의 조합은 유일해야 함
                )
        }
)

@Getter
@Entity
public class DisposalSchedule { // 배출일정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String disposal_day;

    private String disposal_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    //값을 생성하는 메서드의 책임은 엔티티에 있음
    public static DisposalSchedule createSchedule
            (Region region, Category category, String disposal_day, String disposal_time) {
        if (region == null) {
            throw new RegionNotFoundException("배출요일은 필수 입력값 입니다.");
        }

        if (category == null) {
            throw new ScheduleException("배출 요일은 필수 입력값 입니다");
        }
        DisposalSchedule schedule = new DisposalSchedule();

        //schedule.region = region; set 메서드에서 이미 할당해줌
        schedule. category = category;
        schedule.disposal_day = disposal_day;
        schedule.disposal_time = disposal_time;

        schedule.setRegion(region);

        return schedule;

    }


    //연관관계 매핑 메서드
    /*
        반대편 외래키도 저장을 한다!
        안하면...? 반대쪽 설정이 안되겠지요...? 그러면 저장시에
        여기는 세팅이 되는데 Region 엔티티는 업데이트가 안되지!
     */
    public void setRegion(Region region) {
        this.region = region;
        if (!region.getSchedules().contains(this)) {
            region.getSchedules().add(this);
        }
    }


}
