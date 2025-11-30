package com.itsutra.project.dao;


import com.itsutra.project.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageDAO extends JpaRepository<Storage, Long> {

    Optional<Storage> findByName(String name);
    List<Storage> findByProvider(String provider);
    List<Storage> findByIsActive(Boolean isActive);
    Optional<Storage> findByIsDefault(Boolean isDefault);

    @Query("SELECT s FROM Storage s WHERE s.isActive = true AND s.quotaBytes > s.usedBytes")
    List<Storage> findAvailableStorages();

    @Query("SELECT s.provider, COUNT(s), SUM(s.usedBytes) FROM Storage s WHERE s.isActive = true GROUP BY s.provider")
    List<Object[]> getStorageUsageByProvider();
}
