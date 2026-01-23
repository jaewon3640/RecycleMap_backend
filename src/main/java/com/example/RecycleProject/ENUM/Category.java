package com.example.RecycleProject.ENUM;

import lombok.Getter;

@Getter
public enum Category {
    // 1. 상수 선언: 여기서 작성한 인자값이 아래 생성자의 매개변수로 전달됩니다.
    PLASTIC("플라스틱", "페트병, 용기류"),
    PAPER("종이", "박스, 신문, 책"),
    STEEL("캔/고철", "음료수캔, 철재"),
    GLASS("유리", "병, 유리용기"),
    WRAP("비닐", "비닐봉투, 랩"),
    STYROFOAM("스티로폼", "포장재, 완충재"),
    FOOD("음식물", "생선류 제외"),
    GENERAL("일반쓰레기", "종량제봉투 사용"),
    ETC("기타", "해당 사항 없음");

    private final String koreanName;
    private final String description;

    // 2. 명시적 생성자: 상수를 선언할 때 넣은 값들을 필드에 할당합니다.
    Category(String koreanName, String description) {
        this.koreanName = koreanName;
        this.description = description;
    }
}
