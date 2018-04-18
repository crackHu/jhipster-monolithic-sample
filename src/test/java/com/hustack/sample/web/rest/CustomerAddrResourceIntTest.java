package com.hustack.sample.web.rest;

import com.hustack.sample.JhipsterMonolithicSampleApp;

import com.hustack.sample.domain.CustomerAddr;
import com.hustack.sample.repository.CustomerAddrRepository;
import com.hustack.sample.service.CustomerAddrService;
import com.hustack.sample.repository.search.CustomerAddrSearchRepository;
import com.hustack.sample.service.dto.CustomerAddrDTO;
import com.hustack.sample.service.mapper.CustomerAddrMapper;
import com.hustack.sample.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static com.hustack.sample.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomerAddrResource REST controller.
 *
 * @see CustomerAddrResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class CustomerAddrResourceIntTest {

    private static final Integer DEFAULT_CUSTOMER_ID = 1;
    private static final Integer UPDATED_CUSTOMER_ID = 2;

    @Autowired
    private CustomerAddrRepository customerAddrRepository;



    @Autowired
    private CustomerAddrMapper customerAddrMapper;
    

    @Autowired
    private CustomerAddrService customerAddrService;

    /**
     * This repository is mocked in the com.hustack.sample.repository.search test package.
     *
     * @see com.hustack.sample.repository.search.CustomerAddrSearchRepositoryMockConfiguration
     */
    @Autowired
    private CustomerAddrSearchRepository mockCustomerAddrSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomerAddrMockMvc;

    private CustomerAddr customerAddr;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerAddrResource customerAddrResource = new CustomerAddrResource(customerAddrService);
        this.restCustomerAddrMockMvc = MockMvcBuilders.standaloneSetup(customerAddrResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerAddr createEntity(EntityManager em) {
        CustomerAddr customerAddr = new CustomerAddr()
            .customerId(DEFAULT_CUSTOMER_ID);
        return customerAddr;
    }

    @Before
    public void initTest() {
        customerAddr = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerAddr() throws Exception {
        int databaseSizeBeforeCreate = customerAddrRepository.findAll().size();

        // Create the CustomerAddr
        CustomerAddrDTO customerAddrDTO = customerAddrMapper.toDto(customerAddr);
        restCustomerAddrMockMvc.perform(post("/api/customer-addrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAddrDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerAddr in the database
        List<CustomerAddr> customerAddrList = customerAddrRepository.findAll();
        assertThat(customerAddrList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerAddr testCustomerAddr = customerAddrList.get(customerAddrList.size() - 1);
        assertThat(testCustomerAddr.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);

        // Validate the CustomerAddr in Elasticsearch
        verify(mockCustomerAddrSearchRepository, times(1)).save(testCustomerAddr);
    }

    @Test
    @Transactional
    public void createCustomerAddrWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerAddrRepository.findAll().size();

        // Create the CustomerAddr with an existing ID
        customerAddr.setId(1L);
        CustomerAddrDTO customerAddrDTO = customerAddrMapper.toDto(customerAddr);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerAddrMockMvc.perform(post("/api/customer-addrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAddrDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerAddr in the database
        List<CustomerAddr> customerAddrList = customerAddrRepository.findAll();
        assertThat(customerAddrList).hasSize(databaseSizeBeforeCreate);

        // Validate the CustomerAddr in Elasticsearch
        verify(mockCustomerAddrSearchRepository, times(0)).save(customerAddr);
    }

    @Test
    @Transactional
    public void getAllCustomerAddrs() throws Exception {
        // Initialize the database
        customerAddrRepository.saveAndFlush(customerAddr);

        // Get all the customerAddrList
        restCustomerAddrMockMvc.perform(get("/api/customer-addrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddr.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)));
    }
    

    @Test
    @Transactional
    public void getCustomerAddr() throws Exception {
        // Initialize the database
        customerAddrRepository.saveAndFlush(customerAddr);

        // Get the customerAddr
        restCustomerAddrMockMvc.perform(get("/api/customer-addrs/{id}", customerAddr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerAddr.getId().intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerAddr() throws Exception {
        // Get the customerAddr
        restCustomerAddrMockMvc.perform(get("/api/customer-addrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerAddr() throws Exception {
        // Initialize the database
        customerAddrRepository.saveAndFlush(customerAddr);

        int databaseSizeBeforeUpdate = customerAddrRepository.findAll().size();

        // Update the customerAddr
        CustomerAddr updatedCustomerAddr = customerAddrRepository.findById(customerAddr.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerAddr are not directly saved in db
        em.detach(updatedCustomerAddr);
        updatedCustomerAddr
            .customerId(UPDATED_CUSTOMER_ID);
        CustomerAddrDTO customerAddrDTO = customerAddrMapper.toDto(updatedCustomerAddr);

        restCustomerAddrMockMvc.perform(put("/api/customer-addrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAddrDTO)))
            .andExpect(status().isOk());

        // Validate the CustomerAddr in the database
        List<CustomerAddr> customerAddrList = customerAddrRepository.findAll();
        assertThat(customerAddrList).hasSize(databaseSizeBeforeUpdate);
        CustomerAddr testCustomerAddr = customerAddrList.get(customerAddrList.size() - 1);
        assertThat(testCustomerAddr.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);

        // Validate the CustomerAddr in Elasticsearch
        verify(mockCustomerAddrSearchRepository, times(1)).save(testCustomerAddr);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerAddr() throws Exception {
        int databaseSizeBeforeUpdate = customerAddrRepository.findAll().size();

        // Create the CustomerAddr
        CustomerAddrDTO customerAddrDTO = customerAddrMapper.toDto(customerAddr);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomerAddrMockMvc.perform(put("/api/customer-addrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerAddrDTO)))
            .andExpect(status().isCreated());

        // Validate the CustomerAddr in the database
        List<CustomerAddr> customerAddrList = customerAddrRepository.findAll();
        assertThat(customerAddrList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the CustomerAddr in Elasticsearch
        verify(mockCustomerAddrSearchRepository, times(0)).save(customerAddr);
    }

    @Test
    @Transactional
    public void deleteCustomerAddr() throws Exception {
        // Initialize the database
        customerAddrRepository.saveAndFlush(customerAddr);

        int databaseSizeBeforeDelete = customerAddrRepository.findAll().size();

        // Get the customerAddr
        restCustomerAddrMockMvc.perform(delete("/api/customer-addrs/{id}", customerAddr.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerAddr> customerAddrList = customerAddrRepository.findAll();
        assertThat(customerAddrList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CustomerAddr in Elasticsearch
        verify(mockCustomerAddrSearchRepository, times(1)).deleteById(customerAddr.getId());
    }

    @Test
    @Transactional
    public void searchCustomerAddr() throws Exception {
        // Initialize the database
        customerAddrRepository.saveAndFlush(customerAddr);
    when(mockCustomerAddrSearchRepository.search(queryStringQuery("id:" + customerAddr.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(customerAddr), PageRequest.of(0, 1), 1));
        // Search the customerAddr
        restCustomerAddrMockMvc.perform(get("/api/_search/customer-addrs?query=id:" + customerAddr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerAddr.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAddr.class);
        CustomerAddr customerAddr1 = new CustomerAddr();
        customerAddr1.setId(1L);
        CustomerAddr customerAddr2 = new CustomerAddr();
        customerAddr2.setId(customerAddr1.getId());
        assertThat(customerAddr1).isEqualTo(customerAddr2);
        customerAddr2.setId(2L);
        assertThat(customerAddr1).isNotEqualTo(customerAddr2);
        customerAddr1.setId(null);
        assertThat(customerAddr1).isNotEqualTo(customerAddr2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAddrDTO.class);
        CustomerAddrDTO customerAddrDTO1 = new CustomerAddrDTO();
        customerAddrDTO1.setId(1L);
        CustomerAddrDTO customerAddrDTO2 = new CustomerAddrDTO();
        assertThat(customerAddrDTO1).isNotEqualTo(customerAddrDTO2);
        customerAddrDTO2.setId(customerAddrDTO1.getId());
        assertThat(customerAddrDTO1).isEqualTo(customerAddrDTO2);
        customerAddrDTO2.setId(2L);
        assertThat(customerAddrDTO1).isNotEqualTo(customerAddrDTO2);
        customerAddrDTO1.setId(null);
        assertThat(customerAddrDTO1).isNotEqualTo(customerAddrDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerAddrMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerAddrMapper.fromId(null)).isNull();
    }
}
