package com.hustack.sample.repository.search;

import com.hustack.sample.domain.Travelrecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Travelrecord entity.
 */
public interface TravelrecordSearchRepository extends ElasticsearchRepository<Travelrecord, Long> {
}
