package com.example.leavestech.web.rest;

import static com.example.leavestech.domain.CourseEnrollmentAsserts.*;
import static com.example.leavestech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.leavestech.IntegrationTest;
import com.example.leavestech.domain.Course;
import com.example.leavestech.domain.CourseEnrollment;
import com.example.leavestech.domain.Student;
import com.example.leavestech.domain.enumeration.EnrollmentStatus;
import com.example.leavestech.repository.CourseEnrollmentRepository;
import com.example.leavestech.service.dto.CourseEnrollmentDTO;
import com.example.leavestech.service.mapper.CourseEnrollmentMapper;
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
 * Integration tests for the {@link CourseEnrollmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseEnrollmentResourceIT {

    private static final EnrollmentStatus DEFAULT_STATUS = EnrollmentStatus.ACTIVE;
    private static final EnrollmentStatus UPDATED_STATUS = EnrollmentStatus.DROPPED;

    private static final String ENTITY_API_URL = "/api/course-enrollments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private CourseEnrollmentMapper courseEnrollmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseEnrollmentMockMvc;

    private CourseEnrollment courseEnrollment;

    private CourseEnrollment insertedCourseEnrollment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEnrollment createEntity(EntityManager em) {
        CourseEnrollment courseEnrollment = new CourseEnrollment().status(DEFAULT_STATUS);
        // Add required entity
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            course = CourseResourceIT.createEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        courseEnrollment.setCourse(course);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        courseEnrollment.setStudent(student);
        return courseEnrollment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEnrollment createUpdatedEntity(EntityManager em) {
        CourseEnrollment updatedCourseEnrollment = new CourseEnrollment().status(UPDATED_STATUS);
        // Add required entity
        Course course;
        if (TestUtil.findAll(em, Course.class).isEmpty()) {
            course = CourseResourceIT.createUpdatedEntity(em);
            em.persist(course);
            em.flush();
        } else {
            course = TestUtil.findAll(em, Course.class).get(0);
        }
        updatedCourseEnrollment.setCourse(course);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createUpdatedEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        updatedCourseEnrollment.setStudent(student);
        return updatedCourseEnrollment;
    }

    @BeforeEach
    void initTest() {
        courseEnrollment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCourseEnrollment != null) {
            courseEnrollmentRepository.delete(insertedCourseEnrollment);
            insertedCourseEnrollment = null;
        }
    }

    @Test
    @Transactional
    void createCourseEnrollment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);
        var returnedCourseEnrollmentDTO = om.readValue(
            restCourseEnrollmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseEnrollmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CourseEnrollmentDTO.class
        );

        // Validate the CourseEnrollment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourseEnrollment = courseEnrollmentMapper.toEntity(returnedCourseEnrollmentDTO);
        assertCourseEnrollmentUpdatableFieldsEquals(returnedCourseEnrollment, getPersistedCourseEnrollment(returnedCourseEnrollment));

        insertedCourseEnrollment = returnedCourseEnrollment;
    }

    @Test
    @Transactional
    void createCourseEnrollmentWithExistingId() throws Exception {
        // Create the CourseEnrollment with an existing ID
        courseEnrollment.setId(1L);
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseEnrollmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courseEnrollment.setStatus(null);

        // Create the CourseEnrollment, which fails.
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        restCourseEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseEnrollmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseEnrollments() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        // Get all the courseEnrollmentList
        restCourseEnrollmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseEnrollment.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getCourseEnrollment() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        // Get the courseEnrollment
        restCourseEnrollmentMockMvc
            .perform(get(ENTITY_API_URL_ID, courseEnrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseEnrollment.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourseEnrollment() throws Exception {
        // Get the courseEnrollment
        restCourseEnrollmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourseEnrollment() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseEnrollment
        CourseEnrollment updatedCourseEnrollment = courseEnrollmentRepository.findById(courseEnrollment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourseEnrollment are not directly saved in db
        em.detach(updatedCourseEnrollment);
        updatedCourseEnrollment.status(UPDATED_STATUS);
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(updatedCourseEnrollment);

        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseEnrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseEnrollmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourseEnrollmentToMatchAllProperties(updatedCourseEnrollment);
    }

    @Test
    @Transactional
    void putNonExistingCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseEnrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseEnrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseEnrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseEnrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseEnrollmentWithPatch() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseEnrollment using partial update
        CourseEnrollment partialUpdatedCourseEnrollment = new CourseEnrollment();
        partialUpdatedCourseEnrollment.setId(courseEnrollment.getId());

        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourseEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseEnrollmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCourseEnrollment, courseEnrollment),
            getPersistedCourseEnrollment(courseEnrollment)
        );
    }

    @Test
    @Transactional
    void fullUpdateCourseEnrollmentWithPatch() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseEnrollment using partial update
        CourseEnrollment partialUpdatedCourseEnrollment = new CourseEnrollment();
        partialUpdatedCourseEnrollment.setId(courseEnrollment.getId());

        partialUpdatedCourseEnrollment.status(UPDATED_STATUS);

        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourseEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the CourseEnrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseEnrollmentUpdatableFieldsEquals(
            partialUpdatedCourseEnrollment,
            getPersistedCourseEnrollment(partialUpdatedCourseEnrollment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseEnrollmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courseEnrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courseEnrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseEnrollment.setId(longCount.incrementAndGet());

        // Create the CourseEnrollment
        CourseEnrollmentDTO courseEnrollmentDTO = courseEnrollmentMapper.toDto(courseEnrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEnrollmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(courseEnrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEnrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseEnrollment() throws Exception {
        // Initialize the database
        insertedCourseEnrollment = courseEnrollmentRepository.saveAndFlush(courseEnrollment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the courseEnrollment
        restCourseEnrollmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseEnrollment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return courseEnrollmentRepository.count();
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

    protected CourseEnrollment getPersistedCourseEnrollment(CourseEnrollment courseEnrollment) {
        return courseEnrollmentRepository.findById(courseEnrollment.getId()).orElseThrow();
    }

    protected void assertPersistedCourseEnrollmentToMatchAllProperties(CourseEnrollment expectedCourseEnrollment) {
        assertCourseEnrollmentAllPropertiesEquals(expectedCourseEnrollment, getPersistedCourseEnrollment(expectedCourseEnrollment));
    }

    protected void assertPersistedCourseEnrollmentToMatchUpdatableProperties(CourseEnrollment expectedCourseEnrollment) {
        assertCourseEnrollmentAllUpdatablePropertiesEquals(
            expectedCourseEnrollment,
            getPersistedCourseEnrollment(expectedCourseEnrollment)
        );
    }
}
