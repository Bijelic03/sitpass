package com.ftn.sitpass.ues;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FacilitySearchRepository extends ElasticsearchRepository<FacilitySearchDocument, Long> {
}
