package com.interview.project.job.controller;//package com.itsutra.project.job.controller;
//
//
//import com.itsutra.project.job.dto.LocationRequestDTO;
//import com.itsutra.project.job.dto.LocationResponseDTO;
//import com.itsutra.project.job.service.LocationService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/interviews/locations")
//@RequiredArgsConstructor
//public class LocationController {
//
//    private final LocationService locationService;
//
//    @PostMapping
//    public ResponseEntity<LocationResponseDTO> createLocation(@Valid @RequestBody LocationRequestDTO request) {
//        LocationResponseDTO response = locationService.createLocation(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable Long id) {
//        LocationResponseDTO response = locationService.getLocationById(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
//        List<LocationResponseDTO> locations = locationService.getAllLocations();
//        return ResponseEntity.ok(locations);
//    }
//
//
//    @GetMapping("/active")
//    public ResponseEntity<List<LocationResponseDTO>> getActiveLocations() {
//        List<LocationResponseDTO> locations = locationService.getActiveLocations();
//        return ResponseEntity.ok(locations);
//    }
//
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<LocationResponseDTO> updateLocation(
//            @PathVariable Long id,
//            @Valid @RequestBody LocationRequestDTO request) {
//
//        LocationResponseDTO response = locationService.updateLocation(id, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
//        locationService.deleteLocation(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @GetMapping("/country/{country}")
//    public ResponseEntity<List<LocationResponseDTO>> getLocationsByCountry(@PathVariable String country) {
//        List<LocationResponseDTO> locations = locationService.getLocationsByCountry(country);
//        return ResponseEntity.ok(locations);
//    }
//
//    @GetMapping("/city/{city}")
//    public ResponseEntity<List<LocationResponseDTO>> getLocationsByCity(@PathVariable String city) {
//        List<LocationResponseDTO> locations = locationService.getLocationsByCity(city);
//        return ResponseEntity.ok(locations);
//    }
//
//
//
//    @GetMapping("/remote")
//    public ResponseEntity<List<LocationResponseDTO>> getRemoteLocations() {
//        List<LocationResponseDTO> locations = locationService.getRemoteLocations();
//        return ResponseEntity.ok(locations);
//    }
//
//
//
//
//    @GetMapping("/countries")
//    public ResponseEntity<List<String>> getAvailableCountries() {
//        List<String> countries = locationService.getAvailableCountries();
//        return ResponseEntity.ok(countries);
//    }
//
//
//
//    @GetMapping("/countries/{country}/cities")
//    public ResponseEntity<List<String>> getCitiesByCountry(@PathVariable String country) {
//        List<String> cities = locationService.getCitiesByCountry(country);
//        return ResponseEntity.ok(cities);
//    }
//
//
//
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<LocationResponseDTO> toggleLocationStatus(
//            @PathVariable Long id,
//            @RequestBody Map<String, Boolean> request) {
//
//        Boolean isActive = request.get("isActive");
//        LocationResponseDTO response = locationService.toggleLocationStatus(id, isActive);
//        return ResponseEntity.ok(response);
//    }
//}
