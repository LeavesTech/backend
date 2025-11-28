package com.example.leavestech.web.rest;

import static com.example.leavestech.domain.SubmissionAsserts.*;
import static com.example.leavestech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.leavestech.IntegrationTest;
import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.domain.Exam;
import com.example.leavestech.domain.Student;
import com.example.leavestech.domain.Submission;
import com.example.leavestech.domain.enumeration.SubmissionStatus;
import com.example.leavestech.repository.SubmissionRepository;
import com.example.leavestech.service.dto.SubmissionDTO;
import com.example.leavestech.service.mapper.SubmissionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SubmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionResourceIT {

    private static final String DEFAULT_SUBMITTED_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SUBMITTED_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final SubmissionStatus DEFAULT_STATUS = SubmissionStatus.PENDING;
    private static final SubmissionStatus UPDATED_STATUS = SubmissionStatus.PASSED;

    private static final Instant DEFAULT_SUBMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    private Submission insertedSubmission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
            .submittedCode(DEFAULT_SUBMITTED_CODE)
            .score(DEFAULT_SCORE)
            .status(DEFAULT_STATUS)
            .submissionDate(DEFAULT_SUBMISSION_DATE);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        submission.setStudent(student);
        // Add required entity
        CodingQuestion codingQuestion;
        if (TestUtil.findAll(em, CodingQuestion.class).isEmpty()) {
            codingQuestion = CodingQuestionResourceIT.createEntity();
            em.persist(codingQuestion);
            em.flush();
        } else {
            codingQuestion = TestUtil.findAll(em, CodingQuestion.class).get(0);
        }
        submission.setQuestion(codingQuestion);
        // Add required entity
        Exam exam;
        if (TestUtil.findAll(em, Exam.class).isEmpty()) {
            exam = ExamResourceIT.createEntity(em);
            em.persist(exam);
            em.flush();
        } else {
            exam = TestUtil.findAll(em, Exam.class).get(0);
        }
        submission.setExam(exam);
        return submission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createUpdatedEntity(EntityManager em) {
        Submission updatedSubmission = new Submission()
            .submittedCode(UPDATED_SUBMITTED_CODE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .submissionDate(UPDATED_SUBMISSION_DATE);
        // Add required entity
        Student student;
        if (TestUtil.findAll(em, Student.class).isEmpty()) {
            student = StudentResourceIT.createUpdatedEntity();
            em.persist(student);
            em.flush();
        } else {
            student = TestUtil.findAll(em, Student.class).get(0);
        }
        updatedSubmission.setStudent(student);
        // Add required entity
        CodingQuestion codingQuestion;
        if (TestUtil.findAll(em, CodingQuestion.class).isEmpty()) {
            codingQuestion = CodingQuestionResourceIT.createUpdatedEntity();
            em.persist(codingQuestion);
            em.flush();
        } else {
            codingQuestion = TestUtil.findAll(em, CodingQuestion.class).get(0);
        }
        updatedSubmission.setQuestion(codingQuestion);
        // Add required entity
        Exam exam;
        if (TestUtil.findAll(em, Exam.class).isEmpty()) {
            exam = ExamResourceIT.createUpdatedEntity(em);
            em.persist(exam);
            em.flush();
        } else {
            exam = TestUtil.findAll(em, Exam.class).get(0);
        }
        updatedSubmission.setExam(exam);
        return updatedSubmission;
    }

    @BeforeEach
    void initTest() {
        submission = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSubmission != null) {
            submissionRepository.delete(insertedSubmission);
            insertedSubmission = null;
        }
    }

    @Test
    @Transactional
    void createSubmission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        var returnedSubmissionDTO = om.readValue(
            restSubmissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubmissionDTO.class
        );

        // Validate the Submission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubmission = submissionMapper.toEntity(returnedSubmissionDTO);
        assertSubmissionUpdatableFieldsEquals(returnedSubmission, getPersistedSubmission(returnedSubmission));

        insertedSubmission = returnedSubmission;
    }

    @Test
    @Transactional
    void createSubmissionWithExistingId() throws Exception {
        // Create the Submission with an existing ID
        submission.setId(1L);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubmittedCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setSubmittedCode(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setStatus(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        submission.setSubmissionDate(null);

        // Create the Submission, which fails.
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubmissions() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].submittedCode").value(hasItem(DEFAULT_SUBMITTED_CODE)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())));
    }

    @Test
    @Transactional
    void getSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.submittedCode").value(DEFAULT_SUBMITTED_CODE))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findById(submission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
            .submittedCode(UPDATED_SUBMITTED_CODE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .submissionDate(UPDATED_SUBMISSION_DATE);
        SubmissionDTO submissionDTO = submissionMapper.toDto(updatedSubmission);

        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubmissionToMatchAllProperties(updatedSubmission);
    }

    @Test
    @Transactional
    void putNonExistingSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission.submittedCode(UPDATED_SUBMITTED_CODE).status(UPDATED_STATUS);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubmission, submission),
            getPersistedSubmission(submission)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .submittedCode(UPDATED_SUBMITTED_CODE)
            .score(UPDATED_SCORE)
            .status(UPDATED_STATUS)
            .submissionDate(UPDATED_SUBMISSION_DATE);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubmissionUpdatableFieldsEquals(partialUpdatedSubmission, getPersistedSubmission(partialUpdatedSubmission));
    }

    @Test
    @Transactional
    void patchNonExistingSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        submission.setId(longCount.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmission() throws Exception {
        // Initialize the database
        insertedSubmission = submissionRepository.saveAndFlush(submission);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the submission
        restSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, submission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return submissionRepository.count();
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

    protected Submission getPersistedSubmission(Submission submission) {
        return submissionRepository.findById(submission.getId()).orElseThrow();
    }

    protected void assertPersistedSubmissionToMatchAllProperties(Submission expectedSubmission) {
        assertSubmissionAllPropertiesEquals(expectedSubmission, getPersistedSubmission(expectedSubmission));
    }

    protected void assertPersistedSubmissionToMatchUpdatableProperties(Submission expectedSubmission) {
        assertSubmissionAllUpdatablePropertiesEquals(expectedSubmission, getPersistedSubmission(expectedSubmission));
    }
}
