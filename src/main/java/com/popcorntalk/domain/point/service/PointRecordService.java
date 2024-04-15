package com.popcorntalk.domain.point.service;

public interface PointRecordService {

    /**
     * 포인트 기록 생성 메서드 포인트 생성 및 DB 저장
     *
     * @param pointId       포인트 Id
     * @param previousPoint 이전 포인트
     * @param amount        포인트
     * @param finalPoint    이후 포인트
     */
    void createPointRecord(Long pointId, int previousPoint, int amount, int finalPoint);
}
