package com.ddiring.Backend_Monitoring.repository;

import com.ddiring.Backend_Monitoring.entity.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringRepository extends JpaRepository<Monitoring, Integer> {
    List<Monitoring> findByWriterId(String writerId);
}
