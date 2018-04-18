package com.hustack.sample.web.rest;

import com.hustack.sample.JhipsterMonolithicSampleApp;

import com.hustack.sample.domain.OrderItems;
import com.hustack.sample.repository.OrderItemsRepository;
import com.hustack.sample.service.OrderItemsService;
import com.hustack.sample.repository.search.OrderItemsSearchRepository;
import com.hustack.sample.service.dto.OrderItemsDTO;
import com.hustack.sample.service.mapper.OrderItemsMapper;
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
 * Test class for the OrderItemsResource REST controller.
 *
 * @see OrderItemsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class OrderItemsResourceIntTest {

    private static final Integer DEFAULT_ORDER_ID = 1;
    private static final Integer UPDATED_ORDER_ID = 2;

    @Autowired
    private OrderItemsRepository orderItemsRepository;



    @Autowired
    private OrderItemsMapper orderItemsMapper;
    

    @Autowired
    private OrderItemsService orderItemsService;

    /**
     * This repository is mocked in the com.hustack.sample.repository.search test package.
     *
     * @see com.hustack.sample.repository.search.OrderItemsSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderItemsSearchRepository mockOrderItemsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOrderItemsMockMvc;

    private OrderItems orderItems;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderItemsResource orderItemsResource = new OrderItemsResource(orderItemsService);
        this.restOrderItemsMockMvc = MockMvcBuilders.standaloneSetup(orderItemsResource)
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
    public static OrderItems createEntity(EntityManager em) {
        OrderItems orderItems = new OrderItems()
            .orderId(DEFAULT_ORDER_ID);
        return orderItems;
    }

    @Before
    public void initTest() {
        orderItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderItems() throws Exception {
        int databaseSizeBeforeCreate = orderItemsRepository.findAll().size();

        // Create the OrderItems
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);
        restOrderItemsMockMvc.perform(post("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeCreate + 1);
        OrderItems testOrderItems = orderItemsList.get(orderItemsList.size() - 1);
        assertThat(testOrderItems.getOrderId()).isEqualTo(DEFAULT_ORDER_ID);

        // Validate the OrderItems in Elasticsearch
        verify(mockOrderItemsSearchRepository, times(1)).save(testOrderItems);
    }

    @Test
    @Transactional
    public void createOrderItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderItemsRepository.findAll().size();

        // Create the OrderItems with an existing ID
        orderItems.setId(1L);
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderItemsMockMvc.perform(post("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeCreate);

        // Validate the OrderItems in Elasticsearch
        verify(mockOrderItemsSearchRepository, times(0)).save(orderItems);
    }

    @Test
    @Transactional
    public void getAllOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        // Get all the orderItemsList
        restOrderItemsMockMvc.perform(get("/api/order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)));
    }
    

    @Test
    @Transactional
    public void getOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        // Get the orderItems
        restOrderItemsMockMvc.perform(get("/api/order-items/{id}", orderItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderItems.getId().intValue()))
            .andExpect(jsonPath("$.orderId").value(DEFAULT_ORDER_ID));
    }

    @Test
    @Transactional
    public void getNonExistingOrderItems() throws Exception {
        // Get the orderItems
        restOrderItemsMockMvc.perform(get("/api/order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        int databaseSizeBeforeUpdate = orderItemsRepository.findAll().size();

        // Update the orderItems
        OrderItems updatedOrderItems = orderItemsRepository.findById(orderItems.getId()).get();
        // Disconnect from session so that the updates on updatedOrderItems are not directly saved in db
        em.detach(updatedOrderItems);
        updatedOrderItems
            .orderId(UPDATED_ORDER_ID);
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(updatedOrderItems);

        restOrderItemsMockMvc.perform(put("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isOk());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeUpdate);
        OrderItems testOrderItems = orderItemsList.get(orderItemsList.size() - 1);
        assertThat(testOrderItems.getOrderId()).isEqualTo(UPDATED_ORDER_ID);

        // Validate the OrderItems in Elasticsearch
        verify(mockOrderItemsSearchRepository, times(1)).save(testOrderItems);
    }

    @Test
    @Transactional
    public void updateNonExistingOrderItems() throws Exception {
        int databaseSizeBeforeUpdate = orderItemsRepository.findAll().size();

        // Create the OrderItems
        OrderItemsDTO orderItemsDTO = orderItemsMapper.toDto(orderItems);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOrderItemsMockMvc.perform(put("/api/order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the OrderItems in the database
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the OrderItems in Elasticsearch
        verify(mockOrderItemsSearchRepository, times(0)).save(orderItems);
    }

    @Test
    @Transactional
    public void deleteOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);

        int databaseSizeBeforeDelete = orderItemsRepository.findAll().size();

        // Get the orderItems
        restOrderItemsMockMvc.perform(delete("/api/order-items/{id}", orderItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderItems> orderItemsList = orderItemsRepository.findAll();
        assertThat(orderItemsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OrderItems in Elasticsearch
        verify(mockOrderItemsSearchRepository, times(1)).deleteById(orderItems.getId());
    }

    @Test
    @Transactional
    public void searchOrderItems() throws Exception {
        // Initialize the database
        orderItemsRepository.saveAndFlush(orderItems);
    when(mockOrderItemsSearchRepository.search(queryStringQuery("id:" + orderItems.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(orderItems), PageRequest.of(0, 1), 1));
        // Search the orderItems
        restOrderItemsMockMvc.perform(get("/api/_search/order-items?query=id:" + orderItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderId").value(hasItem(DEFAULT_ORDER_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItems.class);
        OrderItems orderItems1 = new OrderItems();
        orderItems1.setId(1L);
        OrderItems orderItems2 = new OrderItems();
        orderItems2.setId(orderItems1.getId());
        assertThat(orderItems1).isEqualTo(orderItems2);
        orderItems2.setId(2L);
        assertThat(orderItems1).isNotEqualTo(orderItems2);
        orderItems1.setId(null);
        assertThat(orderItems1).isNotEqualTo(orderItems2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItemsDTO.class);
        OrderItemsDTO orderItemsDTO1 = new OrderItemsDTO();
        orderItemsDTO1.setId(1L);
        OrderItemsDTO orderItemsDTO2 = new OrderItemsDTO();
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
        orderItemsDTO2.setId(orderItemsDTO1.getId());
        assertThat(orderItemsDTO1).isEqualTo(orderItemsDTO2);
        orderItemsDTO2.setId(2L);
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
        orderItemsDTO1.setId(null);
        assertThat(orderItemsDTO1).isNotEqualTo(orderItemsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(orderItemsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(orderItemsMapper.fromId(null)).isNull();
    }
}
