package com.example.leavestech.web.rest;

import static com.example.leavestech.domain.TeacherAsserts.*;
import static com.example.leavestech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.leavestech.IntegrationTest;
import com.example.leavestech.domain.Teacher;
import com.example.leavestech.repository.TeacherRepository;
import com.example.leavestech.service.dto.TeacherDTO;
import com.example.leavestech.service.mapper.TeacherMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TeacherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeacherResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_ROOM = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_ROOM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teachers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    private Teacher insertedTeacher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createEntity() {
        return new Teacher().title(DEFAULT_TITLE).officeRoom(DEFAULT_OFFICE_ROOM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Teacher createUpdatedEntity() {
        return new Teacher().title(UPDATED_TITLE).officeRoom(UPDATED_OFFICE_ROOM);
    }

    @BeforeEach
    void initTest() {
        teacher = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTeacher != null) {
            teacherRepository.delete(insertedTeacher);
            insertedTeacher = null;
        }
    }

    @Test
    @Transactional
    void createTeacher() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);
        var returnedTeacherDTO = om.readValue(
            restTeacherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TeacherDTO.class
        );

        // Validate the Teacher in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTeacher = teacherMapper.toEntity(returnedTeacherDTO);
        assertTeacherUpdatableFieldsEquals(returnedTeacher, getPersistedTeacher(returnedTeacher));

        insertedTeacher = returnedTeacher;
    }

    @Test
    @Transactional
    void createTeacherWithExistingId() throws Exception {
        // Create the Teacher with an existing ID
        teacher.setId(1L);
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeacherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTeachers() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        // Get all the teacherList
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].officeRoom").value(hasItem(DEFAULT_OFFICE_ROOM)));
    }

    @Test
    @Transactional
    void getTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc
            .perform(get(ENTITY_API_URL_ID, teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.officeRoom").value(DEFAULT_OFFICE_ROOM));
    }

    @Test
    @Transactional
    void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher
        Teacher updatedTeacher = teacherRepository.findById(teacher.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeacher are not directly saved in db
        em.detach(updatedTeacher);
        updatedTeacher.title(UPDATED_TITLE).officeRoom(UPDATED_OFFICE_ROOM);
        TeacherDTO teacherDTO = teacherMapper.toDto(updatedTeacher);

        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacherDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherDTO))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeacherToMatchAllProperties(updatedTeacher);
    }

    @Test
    @Transactional
    void putNonExistingTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teacherDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teacherDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teacherDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.title(UPDATED_TITLE).officeRoom(UPDATED_OFFICE_ROOM);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTeacher, teacher), getPersistedTeacher(teacher));
    }

    @Test
    @Transactional
    void fullUpdateTeacherWithPatch() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teacher using partial update
        Teacher partialUpdatedTeacher = new Teacher();
        partialUpdatedTeacher.setId(teacher.getId());

        partialUpdatedTeacher.title(UPDATED_TITLE).officeRoom(UPDATED_OFFICE_ROOM);

        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeacher.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeacher))
            )
            .andExpect(status().isOk());

        // Validate the Teacher in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeacherUpdatableFieldsEquals(partialUpdatedTeacher, getPersistedTeacher(partialUpdatedTeacher));
    }

    @Test
    @Transactional
    void patchNonExistingTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teacherDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teacherDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teacherDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeacher() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teacher.setId(longCount.incrementAndGet());

        // Create the Teacher
        TeacherDTO teacherDTO = teacherMapper.toDto(teacher);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeacherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teacherDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Teacher in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeacher() throws Exception {
        // Initialize the database
        insertedTeacher = teacherRepository.saveAndFlush(teacher);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teacher
        restTeacherMockMvc
            .perform(delete(ENTITY_API_URL_ID, teacher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teacherRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Teacher getPersistedTeacher(Teacher teacher) {
        return teacherRepository.findById(teacher.getId()).orElseThrow();
    }

    protected void assertPersistedTeacherToMatchAllProperties(Teacher expectedTeacher) {
        assertTeacherAllPropertiesEquals(expectedTeacher, getPersistedTeacher(expectedTeacher));
    }

    protected void assertPersistedTeacherToMatchUpdatableProperties(Teacher expectedTeacher) {
        assertTeacherAllUpdatablePropertiesEquals(expectedTeacher, getPersistedTeacher(expectedTeacher));
    }
}
