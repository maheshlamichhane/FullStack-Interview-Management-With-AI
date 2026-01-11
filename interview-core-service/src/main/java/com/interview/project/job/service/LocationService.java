package com.interview.project.job.service;//package com.itsutra.project.job.service;
//
//
//import com.itsutra.project.job.dao.LocationDAO;
//import com.itsutra.project.job.dto.LocationRequestDTO;
//import com.itsutra.project.job.dto.LocationResponseDTO;
//import com.itsutra.project.job.entity.Location;
//import com.itsutra.project.job.mapper.JobMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//@Slf4j
//public class LocationService {
//
//    private final LocationDAO locationDAO;
//    private final JobMapper jobMapper;
//
//
//    @Transactional
//    public LocationResponseDTO createLocation(LocationRequestDTO request) {
//        log.info("Creating new location: {}", request.getName());
//
//        // Validate unique name and code
//        if (locationDAO.existsByName(request.getName())) {
//            throw new IllegalArgumentException("Location name already exists: " + request.getName());
//        }
//
//        if (request.getCode() != null && locationDAO.existsByCode(request.getCode())) {
//            throw new IllegalArgumentException("Location code already exists: " + request.getCode());
//        }
//
//        Location location = jobMapper.toLocationEntity(request);
//        Location savedLocation = locationDAO.save(location);
//
//        log.info("Successfully created location with id: {}", savedLocation.getId());
//        return jobMapper.toLocationResponse(savedLocation);
//    }
//
//
//    @Transactional(readOnly = true)
//    public LocationResponseDTO getLocationById(Long id) {
//        log.debug("Fetching location by id: {}", id);
//        Location location = locationDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
//        return jobMapper.toLocationResponse(location);
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public List<LocationResponseDTO> getAllLocations() {
//        log.debug("Fetching all locations");
//        return locationDAO.findAll().stream()
//                .map(jobMapper::toLocationResponse)
//                .collect(Collectors.toList());
//    }
//
//    // Get Active Locations
//    @Transactional(readOnly = true)
//    public List<LocationResponseDTO> getActiveLocations() {
//        log.debug("Fetching active locations");
//        return locationDAO.findByIsActive(true).stream()
//                .map(jobMapper::toLocationResponse)
//                .collect(Collectors.toList());
//    }
//
//
//
//
//    @Transactional
//    public LocationResponseDTO updateLocation(Long id, LocationRequestDTO request) {
//        log.info("Updating location with id: {}", id);
//
//        Location location = locationDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
//
//        // Check for duplicate name
//        if (request.getName() != null && !request.getName().equals(location.getName())) {
//            if (locationDAO.existsByName(request.getName())) {
//                throw new IllegalArgumentException("Location name already exists: " + request.getName());
//            }
//            location.setName(request.getName());
//        }
//
//        // Check for duplicate code
//        if (request.getCode() != null && !request.getCode().equals(location.getCode())) {
//            if (locationDAO.existsByCode(request.getCode())) {
//                throw new IllegalArgumentException("Location code already exists: " + request.getCode());
//            }
//            location.setCode(request.getCode());
//        }
//
//        // Update other fields
//        Optional.ofNullable(request.getAddress()).ifPresent(location::setAddress);
//        Optional.ofNullable(request.getCity()).ifPresent(location::setCity);
//        Optional.ofNullable(request.getState()).ifPresent(location::setState);
//        Optional.ofNullable(request.getCountry()).ifPresent(location::setCountry);
//        Optional.ofNullable(request.getPostalCode()).ifPresent(location::setPostalCode);
//        Optional.ofNullable(request.getTimezone()).ifPresent(location::setTimezone);
//        Optional.ofNullable(request.getContactPerson()).ifPresent(location::setContactPerson);
//        Optional.ofNullable(request.getContactEmail()).ifPresent(location::setContactEmail);
//        Optional.ofNullable(request.getContactPhone()).ifPresent(location::setContactPhone);
//        Optional.ofNullable(request.isRemote()).ifPresent(location::setIsRemote);
//
//        if (request.getFacilities() != null) {
//            location.setFacilities(jobMapper.convertListToJson(request.getFacilities()));
//        }
//
//        Location updatedLocation = locationDAO.save(location);
//        log.info("Successfully updated location with id: {}", id);
//        return jobMapper.toLocationResponse(updatedLocation);
//    }
//
//
//    @Transactional
//    public void deleteLocation(Long id) {
//        log.info("Deleting location with id: {}", id);
//
//        Location location = locationDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
//
//        // Check if location has job positions
//        if (!location.getJobPositions().isEmpty()) {
//            throw new IllegalStateException("Cannot delete location with associated job positions. Please reassign or delete job positions first.");
//        }
//
//        locationDAO.delete(location);
//        log.info("Successfully deleted location with id: {}", id);
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<LocationResponseDTO> getLocationsByCountry(String country) {
//        log.debug("Fetching locations for country: {}", country);
//        return locationDAO.findActiveByCountry(country).stream()
//                .map(jobMapper::toLocationResponse)
//                .collect(Collectors.toList());
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public List<LocationResponseDTO> getLocationsByCity(String city) {
//        log.debug("Fetching locations for city: {}", city);
//        return locationDAO.findActiveByCity(city).stream()
//                .map(jobMapper::toLocationResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<LocationResponseDTO> getRemoteLocations() {
//        log.debug("Fetching remote locations");
//        return locationDAO.findByIsRemote(true).stream()
//                .map(jobMapper::toLocationResponse)
//                .collect(Collectors.toList());
//    }
//
//
//    @Transactional(readOnly = true)
//    public List<String> getAvailableCountries() {
//        log.debug("Fetching available countries");
//        return locationDAO.findDistinctCountries();
//    }
//
//
//
//    @Transactional(readOnly = true)
//    public List<String> getCitiesByCountry(String country) {
//        log.debug("Fetching cities for country: {}", country);
//        return locationDAO.findCitiesByCountry(country);
//    }
//
//
//
//    @Transactional
//    public LocationResponseDTO toggleLocationStatus(Long id, Boolean isActive) {
//        log.info("Toggling location status for id: {} to {}", id, isActive);
//
//        Location location = locationDAO.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Location not found with id: " + id));
//
//        location.setIsActive(isActive);
//        Location updatedLocation = locationDAO.save(location);
//
//        log.info("Successfully updated location status for id: {} to {}", id, isActive);
//        return jobMapper.toLocationResponse(updatedLocation);
//    }
//}
