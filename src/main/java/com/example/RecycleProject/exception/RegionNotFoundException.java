package com.example.RecycleProject.exception;

// 지역을 찾을 수 없을 때 (이미지의 NotEnoughStockException과 같은 위상)
public class RegionNotFoundException extends BusinessException {
    public RegionNotFoundException() {
        super("해당 지역 정보를 찾을 수 없습니다.");
    }

    public RegionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegionNotFoundException(Throwable cause) {
        super(cause);
    }

    public RegionNotFoundException(String message) {
        super(message);
    }
}
