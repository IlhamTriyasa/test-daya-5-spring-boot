package com.test.dayalima.test.repo;

import com.test.dayalima.test.model.LogApiHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogApiHitRepo extends JpaRepository<LogApiHit, Long> {
    @Query(
            value = "select (select Count(ip_address) from log_api_hit where ip_address = logApi.ip_address) as totalHit, logApi.ip_address as ipAddress, DATE(execution_time) as executionDate from log_api_hit logApi group by logApi.ip_address,executionDate",
            nativeQuery = true
            //value = "select (select Count(la.ipAddress) from LogApiHit la  where la.ipAddress = logApi.ipAddress) as totalHit, logApi.ipAddress from LogApiHit logApi group by logApi.ipAddress"
    )
    List<Object[]> getSummaryApi();
}
