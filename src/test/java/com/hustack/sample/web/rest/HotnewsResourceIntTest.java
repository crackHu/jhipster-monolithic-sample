package com.hustack.sample.web.rest;

import com.hustack.sample.JhipsterMonolithicSampleApp;

import com.hustack.sample.domain.Hotnews;
import com.hustack.sample.repository.HotnewsRepository;
import com.hustack.sample.service.HotnewsService;
import com.hustack.sample.repository.search.HotnewsSearchRepository;
import com.hustack.sample.service.dto.HotnewsDTO;
import com.hustack.sample.service.mapper.HotnewsMapper;
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
 * Test class for the HotnewsResource REST controller.
 *
 * @see HotnewsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class HotnewsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HotnewsRepository hotnewsRepository;



    @Autowired
    private HotnewsMapper hotnewsMapper;
    

    @Autowired
    private HotnewsService hotnewsService;

    /**
     * This repository is mocked in the com.hustack.sample.repository.search test package.
     *
     * @see com.hustack.sample.repository.search.HotnewsSearchRepositoryMockConfiguration
     */
    @Autowired
    private HotnewsSearchRepository mockHotnewsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHotnewsMockMvc;

    private Hotnews hotnews;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HotnewsResource hotnewsResource = new HotnewsResource(hotnewsService);
        this.restHotnewsMockMvc = MockMvcBuilders.standaloneSetup(hotnewsResource)
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
    public static Hotnews createEntity(EntityManager em) {
        Hotnews hotnews = new Hotnews()
            .name(DEFAULT_NAME);
        return hotnews;
    }

    @Before
    public void initTest() {
        hotnews = createEntity(em);
    }

    @Test
    @Transactional
    public void createHotnews() throws Exception {
        int databaseSizeBeforeCreate = hotnewsRepository.findAll().size();

        // Create the Hotnews
        HotnewsDTO hotnewsDTO = hotnewsMapper.toDto(hotnews);
        restHotnewsMockMvc.perform(post("/api/hotnews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotnewsDTO)))
            .andExpect(status().isCreated());

        // Validate the Hotnews in the database
        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeCreate + 1);
        Hotnews testHotnews = hotnewsList.get(hotnewsList.size() - 1);
        assertThat(testHotnews.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Hotnews in Elasticsearch
        verify(mockHotnewsSearchRepository, times(1)).save(testHotnews);
    }

    @Test
    @Transactional
    public void createHotnewsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hotnewsRepository.findAll().size();

        // Create the Hotnews with an existing ID
        hotnews.setId(1L);
        HotnewsDTO hotnewsDTO = hotnewsMapper.toDto(hotnews);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotnewsMockMvc.perform(post("/api/hotnews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotnewsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hotnews in the database
        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Hotnews in Elasticsearch
        verify(mockHotnewsSearchRepository, times(0)).save(hotnews);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotnewsRepository.findAll().size();
        // set the field null
        hotnews.setName(null);

        // Create the Hotnews, which fails.
        HotnewsDTO hotnewsDTO = hotnewsMapper.toDto(hotnews);

        restHotnewsMockMvc.perform(post("/api/hotnews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotnewsDTO)))
            .andExpect(status().isBadRequest());

        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHotnews() throws Exception {
        // Initialize the database
        hotnewsRepository.saveAndFlush(hotnews);

        // Get all the hotnewsList
        restHotnewsMockMvc.perform(get("/api/hotnews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotnews.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    

    @Test
    @Transactional
    public void getHotnews() throws Exception {
        // Initialize the database
        hotnewsRepository.saveAndFlush(hotnews);

        // Get the hotnews
        restHotnewsMockMvc.perform(get("/api/hotnews/{id}", hotnews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hotnews.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHotnews() throws Exception {
        // Get the hotnews
        restHotnewsMockMvc.perform(get("/api/hotnews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHotnews() throws Exception {
        // Initialize the database
        hotnewsRepository.saveAndFlush(hotnews);

        int databaseSizeBeforeUpdate = hotnewsRepository.findAll().size();

        // Update the hotnews
        Hotnews updatedHotnews = hotnewsRepository.findById(hotnews.getId()).get();
        // Disconnect from session so that the updates on updatedHotnews are not directly saved in db
        em.detach(updatedHotnews);
        updatedHotnews
            .name(UPDATED_NAME);
        HotnewsDTO hotnewsDTO = hotnewsMapper.toDto(updatedHotnews);

        restHotnewsMockMvc.perform(put("/api/hotnews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotnewsDTO)))
            .andExpect(status().isOk());

        // Validate the Hotnews in the database
        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeUpdate);
        Hotnews testHotnews = hotnewsList.get(hotnewsList.size() - 1);
        assertThat(testHotnews.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Hotnews in Elasticsearch
        verify(mockHotnewsSearchRepository, times(1)).save(testHotnews);
    }

    @Test
    @Transactional
    public void updateNonExistingHotnews() throws Exception {
        int databaseSizeBeforeUpdate = hotnewsRepository.findAll().size();

        // Create the Hotnews
        HotnewsDTO hotnewsDTO = hotnewsMapper.toDto(hotnews);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHotnewsMockMvc.perform(put("/api/hotnews")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hotnewsDTO)))
            .andExpect(status().isCreated());

        // Validate the Hotnews in the database
        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the Hotnews in Elasticsearch
        verify(mockHotnewsSearchRepository, times(0)).save(hotnews);
    }

    @Test
    @Transactional
    public void deleteHotnews() throws Exception {
        // Initialize the database
        hotnewsRepository.saveAndFlush(hotnews);

        int databaseSizeBeforeDelete = hotnewsRepository.findAll().size();

        // Get the hotnews
        restHotnewsMockMvc.perform(delete("/api/hotnews/{id}", hotnews.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hotnews> hotnewsList = hotnewsRepository.findAll();
        assertThat(hotnewsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Hotnews in Elasticsearch
        verify(mockHotnewsSearchRepository, times(1)).deleteById(hotnews.getId());
    }

    @Test
    @Transactional
    public void searchHotnews() throws Exception {
        // Initialize the database
        hotnewsRepository.saveAndFlush(hotnews);
    when(mockHotnewsSearchRepository.search(queryStringQuery("id:" + hotnews.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(hotnews), PageRequest.of(0, 1), 1));
        // Search the hotnews
        restHotnewsMockMvc.perform(get("/api/_search/hotnews?query=id:" + hotnews.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotnews.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hotnews.class);
        Hotnews hotnews1 = new Hotnews();
        hotnews1.setId(1L);
        Hotnews hotnews2 = new Hotnews();
        hotnews2.setId(hotnews1.getId());
        assertThat(hotnews1).isEqualTo(hotnews2);
        hotnews2.setId(2L);
        assertThat(hotnews1).isNotEqualTo(hotnews2);
        hotnews1.setId(null);
        assertThat(hotnews1).isNotEqualTo(hotnews2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HotnewsDTO.class);
        HotnewsDTO hotnewsDTO1 = new HotnewsDTO();
        hotnewsDTO1.setId(1L);
        HotnewsDTO hotnewsDTO2 = new HotnewsDTO();
        assertThat(hotnewsDTO1).isNotEqualTo(hotnewsDTO2);
        hotnewsDTO2.setId(hotnewsDTO1.getId());
        assertThat(hotnewsDTO1).isEqualTo(hotnewsDTO2);
        hotnewsDTO2.setId(2L);
        assertThat(hotnewsDTO1).isNotEqualTo(hotnewsDTO2);
        hotnewsDTO1.setId(null);
        assertThat(hotnewsDTO1).isNotEqualTo(hotnewsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(hotnewsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(hotnewsMapper.fromId(null)).isNull();
    }
}
