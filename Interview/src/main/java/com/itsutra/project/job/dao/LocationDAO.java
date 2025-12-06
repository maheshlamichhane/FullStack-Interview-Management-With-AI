package com.itsutra.project.job.dao;


import com.itsutra.project.job.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationDAO extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);
    Optional<Location> findByCode(String code);
    Boolean existsByName(String name);
    Boolean existsByCode(String code);

    List<Location> findByCity(String city);
    List<Location> findByCountry(String country);
    List<Location> findByIsActive(Boolean isActive);
    List<Location> findByIsRemote(Boolean isRemote);

    @Query("SELECT l FROM Location l WHERE l.country = :country AND l.isActive = true")
    List<Location> findActiveByCountry(@Param("country") String country);

    @Query("SELECT l FROM Location l WHERE l.city = :city AND l.isActive = true")
    List<Location> findActiveByCity(@Param("city") String city);

    @Query("SELECT DISTINCT l.country FROM Location l WHERE l.isActive = true")
    List<String> findDistinctCountries();

    @Query("SELECT DISTINCT l.city FROM Location l WHERE l.isActive = true AND l.country = :country")
    List<String> findCitiesByCountry(@Param("country") String country);

    @Query("SELECT COUNT(l) FROM Location l WHERE l.isActive = true")
    Long countActiveLocations();
}
