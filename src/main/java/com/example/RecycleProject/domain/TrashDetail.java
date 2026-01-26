package com.example.RecycleProject.domain;

import com.example.RecycleProject.ENUM.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Table(
        name = "trash_detail",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_region_category", // 제약 조건 이름
                        columnNames = {"region_id", "category"} // 이 두 컬럼의 조합은 유일해야 함
                )
        })
@Getter
public class TrashDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String item_name;

    private String disposal_method; // 배출방법

    private String pre_treatment; // 전처리 방법

    private String caution; // 주의 사항

    public static TrashDetail createTrashDetail
            (Region region, Category category,
             String item_name, String disposal_method,
             String pre_treatment, String caution) {
        TrashDetail trashDetail = new TrashDetail();

        trashDetail.region = region;
        trashDetail.category = category;
        trashDetail.item_name = item_name;
        trashDetail.disposal_method = disposal_method;
        trashDetail.pre_treatment = pre_treatment;
        trashDetail.caution = caution;

        return trashDetail;
    }
}
