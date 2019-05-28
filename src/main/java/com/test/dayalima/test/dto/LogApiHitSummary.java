package com.test.dayalima.test.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LogApiHitSummary {
    private Integer totaHit;
    private String ipAddress;
    private Date executionDate;

    public Integer getTotaHit() {
        return totaHit;
    }

    public void setTotaHit(Integer totaHit) {
        this.totaHit = totaHit;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }
}
