package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByFacilityId(Long facilityId);

    long countByFacilityId(Long facilityId);

    @Query("select coalesce(avg(r.rating.staff), 0) from Review r where r.facility.id = :facilityId and (r.hidden = false or r.hidden is null)")
    Double averageStaffByFacilityId(Long facilityId);

    @Query("select coalesce(avg(r.rating.equipment), 0) from Review r where r.facility.id = :facilityId and (r.hidden = false or r.hidden is null)")
    Double averageEquipmentByFacilityId(Long facilityId);

    @Query("select coalesce(avg(r.rating.hygiene), 0) from Review r where r.facility.id = :facilityId and (r.hidden = false or r.hidden is null)")
    Double averageHygieneByFacilityId(Long facilityId);

    @Query("select coalesce(avg(r.rating.space), 0) from Review r where r.facility.id = :facilityId and (r.hidden = false or r.hidden is null)")
    Double averageSpaceByFacilityId(Long facilityId);
}
