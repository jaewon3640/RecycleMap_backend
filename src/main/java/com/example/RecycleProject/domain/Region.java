package com.example.RecycleProject.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name="uk_region", columnNames={"city","district", "dong"})
})

/*
   양방향 매핑을 할 이유가 X 지역 따른 user를 보는것이 아니므로, 단방향
   매핑을 권장함
 */
@Getter
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String city;

    @Column(length = 50)
    private String district;

    @Column(length = 50)
    private String dong;

    public Region() {}

    public static Region createRegion(String city, String district, String dong){
        Region region = new Region();
        region.city = city;
        region.district = district;
        region.dong = dong;

        return region;
    }

    /*
        Trash Detail과 배출 요일은 양방향으로 하여야 서로를 조회 가능하다.
     */

    @OneToMany(mappedBy = "region")
    private List<TrashDetail> details = new ArrayList<>();


    @OneToMany(mappedBy = "region") // 부모의 이름
    private List<DisposalSchedule> schedules = new ArrayList<>();





}
