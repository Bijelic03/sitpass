package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findAllByFacilityId(Long facilityId);

}
