package com.hustack.sample.repository.search;

import com.hustack.sample.domain.CustomerAddr;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerAddr entity.
 */
public interface CustomerAddrSearchRepository extends ElasticsearchRepository<CustomerAddr, Long> {
}
