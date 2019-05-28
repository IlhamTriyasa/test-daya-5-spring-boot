package com.test.dayalima.test.service;

import com.test.dayalima.test.dto.LogApiHitSummary;

import java.util.List;

public interface LogApiHitService {
    void AddLogApiHit(String ipAddress,String url, String responseStatus, String responseMessage);
    List<LogApiHitSummary> getSummary();
}
