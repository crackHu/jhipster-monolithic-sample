package com.hustack.sample.repository.search;

import com.hustack.sample.domain.Hotnews;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Hotnews entity.
 */
public interface HotnewsSearchRepository extends ElasticsearchRepository<Hotnews, Long> {
}
