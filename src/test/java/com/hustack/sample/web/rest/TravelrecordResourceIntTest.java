package com.hustack.sample.web.rest;

import com.hustack.sample.JhipsterMonolithicSampleApp;

import com.hustack.sample.domain.Travelrecord;
import com.hustack.sample.repository.TravelrecordRepository;
import com.hustack.sample.service.TravelrecordService;
import com.hustack.sample.repository.search.TravelrecordSearchRepository;
import com.hustack.sample.service.dto.TravelrecordDTO;
import com.hustack.sample.service.mapper.TravelrecordMapper;
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
 * Test class for the TravelrecordResource REST controller.
 *
 * @see TravelrecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class TravelrecordResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    @Autowired
    private TravelrecordRepository travelrecordRepository;



    @Autowired
    private TravelrecordMapper travelrecordMapper;
    

    @Autowired
    private TravelrecordService travelrecordService;

    /**
     * This repository is mocked in the com.hustack.sample.repository.search test package.
     *
     * @see com.hustack.sample.repository.search.TravelrecordSearchRepositoryMockConfiguration
     */
    @Autowired
    private TravelrecordSearchRepository mockTravelrecordSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTravelrecordMockMvc;

    private Travelrecord travelrecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TravelrecordResource travelrecordResource = new TravelrecordResource(travelrecordService);
        this.restTravelrecordMockMvc = MockMvcBuilders.standaloneSetup(travelrecordResource)
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
    public static Travelrecord createEntity(EntityManager em) {
        Travelrecord travelrecord = new Travelrecord()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE);
        return travelrecord;
    }

    @Before
    public void initTest() {
        travelrecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createTravelrecord() throws Exception {
        int databaseSizeBeforeCreate = travelrecordRepository.findAll().size();

        // Create the Travelrecord
        TravelrecordDTO travelrecordDTO = travelrecordMapper.toDto(travelrecord);
        restTravelrecordMockMvc.perform(post("/api/travelrecords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travelrecordDTO)))
            .andExpect(status().isCreated());

        // Validate the Travelrecord in the database
        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeCreate + 1);
        Travelrecord testTravelrecord = travelrecordList.get(travelrecordList.size() - 1);
        assertThat(testTravelrecord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTravelrecord.getPhone()).isEqualTo(DEFAULT_PHONE);

        // Validate the Travelrecord in Elasticsearch
        verify(mockTravelrecordSearchRepository, times(1)).save(testTravelrecord);
    }

    @Test
    @Transactional
    public void createTravelrecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = travelrecordRepository.findAll().size();

        // Create the Travelrecord with an existing ID
        travelrecord.setId(1L);
        TravelrecordDTO travelrecordDTO = travelrecordMapper.toDto(travelrecord);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTravelrecordMockMvc.perform(post("/api/travelrecords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travelrecordDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Travelrecord in the database
        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeCreate);

        // Validate the Travelrecord in Elasticsearch
        verify(mockTravelrecordSearchRepository, times(0)).save(travelrecord);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = travelrecordRepository.findAll().size();
        // set the field null
        travelrecord.setName(null);

        // Create the Travelrecord, which fails.
        TravelrecordDTO travelrecordDTO = travelrecordMapper.toDto(travelrecord);

        restTravelrecordMockMvc.perform(post("/api/travelrecords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travelrecordDTO)))
            .andExpect(status().isBadRequest());

        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTravelrecords() throws Exception {
        // Initialize the database
        travelrecordRepository.saveAndFlush(travelrecord);

        // Get all the travelrecordList
        restTravelrecordMockMvc.perform(get("/api/travelrecords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travelrecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    

    @Test
    @Transactional
    public void getTravelrecord() throws Exception {
        // Initialize the database
        travelrecordRepository.saveAndFlush(travelrecord);

        // Get the travelrecord
        restTravelrecordMockMvc.perform(get("/api/travelrecords/{id}", travelrecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(travelrecord.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    public void getNonExistingTravelrecord() throws Exception {
        // Get the travelrecord
        restTravelrecordMockMvc.perform(get("/api/travelrecords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravelrecord() throws Exception {
        // Initialize the database
        travelrecordRepository.saveAndFlush(travelrecord);

        int databaseSizeBeforeUpdate = travelrecordRepository.findAll().size();

        // Update the travelrecord
        Travelrecord updatedTravelrecord = travelrecordRepository.findById(travelrecord.getId()).get();
        // Disconnect from session so that the updates on updatedTravelrecord are not directly saved in db
        em.detach(updatedTravelrecord);
        updatedTravelrecord
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE);
        TravelrecordDTO travelrecordDTO = travelrecordMapper.toDto(updatedTravelrecord);

        restTravelrecordMockMvc.perform(put("/api/travelrecords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travelrecordDTO)))
            .andExpect(status().isOk());

        // Validate the Travelrecord in the database
        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeUpdate);
        Travelrecord testTravelrecord = travelrecordList.get(travelrecordList.size() - 1);
        assertThat(testTravelrecord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTravelrecord.getPhone()).isEqualTo(UPDATED_PHONE);

        // Validate the Travelrecord in Elasticsearch
        verify(mockTravelrecordSearchRepository, times(1)).save(testTravelrecord);
    }

    @Test
    @Transactional
    public void updateNonExistingTravelrecord() throws Exception {
        int databaseSizeBeforeUpdate = travelrecordRepository.findAll().size();

        // Create the Travelrecord
        TravelrecordDTO travelrecordDTO = travelrecordMapper.toDto(travelrecord);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTravelrecordMockMvc.perform(put("/api/travelrecords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(travelrecordDTO)))
            .andExpect(status().isCreated());

        // Validate the Travelrecord in the database
        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the Travelrecord in Elasticsearch
        verify(mockTravelrecordSearchRepository, times(0)).save(travelrecord);
    }

    @Test
    @Transactional
    public void deleteTravelrecord() throws Exception {
        // Initialize the database
        travelrecordRepository.saveAndFlush(travelrecord);

        int databaseSizeBeforeDelete = travelrecordRepository.findAll().size();

        // Get the travelrecord
        restTravelrecordMockMvc.perform(delete("/api/travelrecords/{id}", travelrecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Travelrecord> travelrecordList = travelrecordRepository.findAll();
        assertThat(travelrecordList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Travelrecord in Elasticsearch
        verify(mockTravelrecordSearchRepository, times(1)).deleteById(travelrecord.getId());
    }

    @Test
    @Transactional
    public void searchTravelrecord() throws Exception {
        // Initialize the database
        travelrecordRepository.saveAndFlush(travelrecord);
    when(mockTravelrecordSearchRepository.search(queryStringQuery("id:" + travelrecord.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(travelrecord), PageRequest.of(0, 1), 1));
        // Search the travelrecord
        restTravelrecordMockMvc.perform(get("/api/_search/travelrecords?query=id:" + travelrecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(travelrecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Travelrecord.class);
        Travelrecord travelrecord1 = new Travelrecord();
        travelrecord1.setId(1L);
        Travelrecord travelrecord2 = new Travelrecord();
        travelrecord2.setId(travelrecord1.getId());
        assertThat(travelrecord1).isEqualTo(travelrecord2);
        travelrecord2.setId(2L);
        assertThat(travelrecord1).isNotEqualTo(travelrecord2);
        travelrecord1.setId(null);
        assertThat(travelrecord1).isNotEqualTo(travelrecord2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TravelrecordDTO.class);
        TravelrecordDTO travelrecordDTO1 = new TravelrecordDTO();
        travelrecordDTO1.setId(1L);
        TravelrecordDTO travelrecordDTO2 = new TravelrecordDTO();
        assertThat(travelrecordDTO1).isNotEqualTo(travelrecordDTO2);
        travelrecordDTO2.setId(travelrecordDTO1.getId());
        assertThat(travelrecordDTO1).isEqualTo(travelrecordDTO2);
        travelrecordDTO2.setId(2L);
        assertThat(travelrecordDTO1).isNotEqualTo(travelrecordDTO2);
        travelrecordDTO1.setId(null);
        assertThat(travelrecordDTO1).isNotEqualTo(travelrecordDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(travelrecordMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(travelrecordMapper.fromId(null)).isNull();
    }
}
