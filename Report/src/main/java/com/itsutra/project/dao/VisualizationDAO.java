package com.itsutra.project.dao;


import com.itsutra.project.entity.Visualization;
import com.itsutra.project.enums.VisualizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisualizationDAO extends JpaRepository<Visualization, Long> {

//    Optional<Visualization> findByIdAndCreatedById(Long id, Long createdById);

    List<Visualization> findByDashboardId(Long dashboardId);
    List<Visualization> findByReportId(Long reportId);
    List<Visualization> findByType(VisualizationType type);

    @Query("SELECT v FROM Visualization v WHERE v.dashboard.id = :dashboardId AND v.isInteractive = true")
    List<Visualization> findInteractiveVisualizationsByDashboard(@Param("dashboardId") Long dashboardId);

    @Query("SELECT v.type, COUNT(v) FROM Visualization v GROUP BY v.type")
    List<Object[]> countVisualizationsByType();
}
