package com.hustack.sample.web.rest;

import com.hustack.sample.JhipsterMonolithicSampleApp;

import com.hustack.sample.domain.Employee;
import com.hustack.sample.repository.EmployeeRepository;
import com.hustack.sample.service.EmployeeService;
import com.hustack.sample.repository.search.EmployeeSearchRepository;
import com.hustack.sample.service.dto.EmployeeDTO;
import com.hustack.sample.service.mapper.EmployeeMapper;
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
 * Test class for the EmployeeResource REST controller.
 *
 * @see EmployeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipsterMonolithicSampleApp.class)
public class EmployeeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_SHARDING_ID = 1;
    private static final Integer UPDATED_SHARDING_ID = 2;

    @Autowired
    private EmployeeRepository employeeRepository;



    @Autowired
    private EmployeeMapper employeeMapper;
    

    @Autowired
    private EmployeeService employeeService;

    /**
     * This repository is mocked in the com.hustack.sample.repository.search test package.
     *
     * @see com.hustack.sample.repository.search.EmployeeSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmployeeSearchRepository mockEmployeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployeeResource employeeResource = new EmployeeResource(employeeService);
        this.restEmployeeMockMvc = MockMvcBuilders.standaloneSetup(employeeResource)
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
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .name(DEFAULT_NAME)
            .shardingId(DEFAULT_SHARDING_ID);
        return employee;
    }

    @Before
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployee.getShardingId()).isEqualTo(DEFAULT_SHARDING_ID);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    @Transactional
    public void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // Create the Employee with an existing ID
        employee.setId(1L);
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setName(null);

        // Create the Employee, which fails.
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        restEmployeeMockMvc.perform(post("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc.perform(get("/api/employees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shardingId").value(hasItem(DEFAULT_SHARDING_ID)));
    }
    

    @Test
    @Transactional
    public void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shardingId").value(DEFAULT_SHARDING_ID));
    }

    @Test
    @Transactional
    public void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get("/api/employees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .name(UPDATED_NAME)
            .shardingId(UPDATED_SHARDING_ID);
        EmployeeDTO employeeDTO = employeeMapper.toDto(updatedEmployee);

        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEmployee.getShardingId()).isEqualTo(UPDATED_SHARDING_ID);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).save(testEmployee);
    }

    @Test
    @Transactional
    public void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Create the Employee
        EmployeeDTO employeeDTO = employeeMapper.toDto(employee);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEmployeeMockMvc.perform(put("/api/employees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate + 1);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(0)).save(employee);
    }

    @Test
    @Transactional
    public void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Get the employee
        restEmployeeMockMvc.perform(delete("/api/employees/{id}", employee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Employee in Elasticsearch
        verify(mockEmployeeSearchRepository, times(1)).deleteById(employee.getId());
    }

    @Test
    @Transactional
    public void searchEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
    when(mockEmployeeSearchRepository.search(queryStringQuery("id:" + employee.getId()), PageRequest.of(0, 20)))
        .thenReturn(new PageImpl<>(Collections.singletonList(employee), PageRequest.of(0, 1), 1));
        // Search the employee
        restEmployeeMockMvc.perform(get("/api/_search/employees?query=id:" + employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].shardingId").value(hasItem(DEFAULT_SHARDING_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(employee1.getId());
        assertThat(employee1).isEqualTo(employee2);
        employee2.setId(2L);
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.setId(null);
        assertThat(employee1).isNotEqualTo(employee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDTO.class);
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setId(1L);
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO2.setId(employeeDTO1.getId());
        assertThat(employeeDTO1).isEqualTo(employeeDTO2);
        employeeDTO2.setId(2L);
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO1.setId(null);
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(employeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(employeeMapper.fromId(null)).isNull();
    }
}
