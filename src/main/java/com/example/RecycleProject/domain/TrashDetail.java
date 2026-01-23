package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.Category;
import jakarta.persistence.*;

public class TrashDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    private Category category;

    private String item_name;

    private String disposal_method; // 배출방법

    private String pre_treatment; // 전처리 방법

    private String caution; // 주의 사항
}
