package com.test.dayalima.test.service.impl;

import com.test.dayalima.test.dto.LogApiHitSummary;
import com.test.dayalima.test.model.LogApiHit;
import com.test.dayalima.test.repo.LogApiHitRepo;
import com.test.dayalima.test.service.LogApiHitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LogApiHitServiceImpl implements LogApiHitService {

    @Autowired
    LogApiHitRepo logApiHitRepo;

    @Override
    public void AddLogApiHit(String ipAddress, String url, String responseStatus, String responseMessage) {
        LogApiHit logApiHit = new LogApiHit();
        logApiHit.setIpAddress(ipAddress);
        logApiHit.setExecutionTime(new Date());
        logApiHit.setResponseStatus(responseStatus);
        logApiHit.setResponseMessage(responseMessage);
        logApiHit.setUrl(url);

        logApiHitRepo.save(logApiHit);
    }

    @Override
    public List<LogApiHitSummary> getSummary() {
        List<Object[]> logApiHitRepoSummaryApi = logApiHitRepo.getSummaryApi();
        List<LogApiHitSummary> logApiHitSummaryList = new ArrayList<>();
        for(Object[] dataDetail : logApiHitRepoSummaryApi){
            LogApiHitSummary logApiHitSummary = new LogApiHitSummary();
            logApiHitSummary.setTotaHit(Integer.parseInt(String.valueOf(dataDetail[0])));
            logApiHitSummary.setIpAddress(String.valueOf(dataDetail[1]));
            SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
            try {
                logApiHitSummary.setExecutionDate(formatter1.parse(String.valueOf(dataDetail[2])));
            } catch (ParseException e) {
                logApiHitSummary.setExecutionDate(new Date());
            }
            logApiHitSummaryList.add(logApiHitSummary);
        }
        return logApiHitSummaryList;
    }
}
