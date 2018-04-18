package com.hustack.sample.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CustomerAddrSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CustomerAddrSearchRepositoryMockConfiguration {

    @MockBean
    private CustomerAddrSearchRepository mockCustomerAddrSearchRepository;

}
