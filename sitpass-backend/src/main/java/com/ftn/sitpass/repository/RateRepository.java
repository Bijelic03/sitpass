package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.Rate;
import com.ftn.sitpass.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

}
