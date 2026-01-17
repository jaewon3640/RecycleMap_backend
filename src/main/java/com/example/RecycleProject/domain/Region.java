package com.example.RecycleProject.domain;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name="uk_region", columnNames={"city","district"})
})
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

    protected Region() {}


}
