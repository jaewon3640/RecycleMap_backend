package com.example.RecycleProject.domain;

import jakarta.persistence.*;
import lombok.Getter;

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


}
