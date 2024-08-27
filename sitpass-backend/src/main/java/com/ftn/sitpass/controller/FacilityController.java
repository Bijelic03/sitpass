package com.ftn.sitpass.controller;

import com.ftn.sitpass.dto.FacilityDto;
import com.ftn.sitpass.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FacilityDto>> getFacilities() {
        return  ResponseEntity.ok(facilityService.getAll());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FacilityDto> getFacility(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.get(id));
    }

    @PostMapping()
    public ResponseEntity<Void> createFacility(@RequestBody FacilityDto facilityDto) {
        facilityService.create(facilityDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFacility(@PathVariable Long id, @RequestBody FacilityDto facilityDto) {
       facilityService.update(id, facilityDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
