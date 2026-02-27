package com.ftn.sitpass.config;

import com.ftn.sitpass.service.ues.FacilityUesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@RequiredArgsConstructor
public class UesStartupConfig {

    private final FacilityUesService facilityUesService;

    @Bean
    @Order(2)
    public CommandLineRunner reindexFacilitiesOnStartup() {
        return args -> facilityUesService.reindexAllFacilities();
    }
}
