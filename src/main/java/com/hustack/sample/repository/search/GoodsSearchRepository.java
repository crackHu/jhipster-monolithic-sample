package com.hustack.sample.repository.search;

import com.hustack.sample.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Goods entity.
 */
public interface GoodsSearchRepository extends ElasticsearchRepository<Goods, Long> {
}
