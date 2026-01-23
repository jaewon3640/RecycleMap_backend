package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.Category;
import jakarta.persistence.*;

public class DisposalSchedule { // 배출일정

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String disposal_day;
    private String disposal_time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

}
