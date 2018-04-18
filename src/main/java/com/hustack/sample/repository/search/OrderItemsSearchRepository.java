package com.hustack.sample.repository.search;

import com.hustack.sample.domain.OrderItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderItems entity.
 */
public interface OrderItemsSearchRepository extends ElasticsearchRepository<OrderItems, Long> {
}
