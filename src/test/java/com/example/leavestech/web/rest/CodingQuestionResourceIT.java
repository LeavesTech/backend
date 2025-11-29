package com.example.leavestech.web.rest;

import static com.example.leavestech.domain.CodingQuestionAsserts.*;
import static com.example.leavestech.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.leavestech.IntegrationTest;
import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.domain.enumeration.ProgrammingLanguage;
import com.example.leavestech.repository.CodingQuestionRepository;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.service.mapper.CodingQuestionMapper;
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
 * Integration tests for the {@link CodingQuestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CodingQuestionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_SCORE = 1;
    private static final Integer UPDATED_MAX_SCORE = 2;

    private static final String DEFAULT_STARTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_STARTER_CODE = "BBBBBBBBBB";

    private static final ProgrammingLanguage DEFAULT_LANGUAGE = ProgrammingLanguage.C;
    private static final ProgrammingLanguage UPDATED_LANGUAGE = ProgrammingLanguage.JAVA;

    private static final String ENTITY_API_URL = "/api/coding-questions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CodingQuestionRepository codingQuestionRepository;

    @Autowired
    private CodingQuestionMapper codingQuestionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCodingQuestionMockMvc;

    private CodingQuestion codingQuestion;

    private CodingQuestion insertedCodingQuestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodingQuestion createEntity() {
        return new CodingQuestion()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .maxScore(DEFAULT_MAX_SCORE)
            .starterCode(DEFAULT_STARTER_CODE)
            .language(DEFAULT_LANGUAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CodingQuestion createUpdatedEntity() {
        return new CodingQuestion()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .maxScore(UPDATED_MAX_SCORE)
            .starterCode(UPDATED_STARTER_CODE)
            .language(UPDATED_LANGUAGE);
    }

    @BeforeEach
    void initTest() {
        codingQuestion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCodingQuestion != null) {
            codingQuestionRepository.delete(insertedCodingQuestion);
            insertedCodingQuestion = null;
        }
    }

    @Test
    @Transactional
    void createCodingQuestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);
        var returnedCodingQuestionDTO = om.readValue(
            restCodingQuestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CodingQuestionDTO.class
        );

        // Validate the CodingQuestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCodingQuestion = codingQuestionMapper.toEntity(returnedCodingQuestionDTO);
        assertCodingQuestionUpdatableFieldsEquals(returnedCodingQuestion, getPersistedCodingQuestion(returnedCodingQuestion));

        insertedCodingQuestion = returnedCodingQuestion;
    }

    @Test
    @Transactional
    void createCodingQuestionWithExistingId() throws Exception {
        // Create the CodingQuestion with an existing ID
        codingQuestion.setId(1L);
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codingQuestion.setTitle(null);

        // Create the CodingQuestion, which fails.
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codingQuestion.setDescription(null);

        // Create the CodingQuestion, which fails.
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codingQuestion.setMaxScore(null);

        // Create the CodingQuestion, which fails.
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStarterCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codingQuestion.setStarterCode(null);

        // Create the CodingQuestion, which fails.
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        codingQuestion.setLanguage(null);

        // Create the CodingQuestion, which fails.
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        restCodingQuestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCodingQuestions() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        // Get all the codingQuestionList
        restCodingQuestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(codingQuestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].maxScore").value(hasItem(DEFAULT_MAX_SCORE)))
            .andExpect(jsonPath("$.[*].starterCode").value(hasItem(DEFAULT_STARTER_CODE)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    void getCodingQuestion() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        // Get the codingQuestion
        restCodingQuestionMockMvc
            .perform(get(ENTITY_API_URL_ID, codingQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(codingQuestion.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.maxScore").value(DEFAULT_MAX_SCORE))
            .andExpect(jsonPath("$.starterCode").value(DEFAULT_STARTER_CODE))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCodingQuestion() throws Exception {
        // Get the codingQuestion
        restCodingQuestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCodingQuestion() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codingQuestion
        CodingQuestion updatedCodingQuestion = codingQuestionRepository.findById(codingQuestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCodingQuestion are not directly saved in db
        em.detach(updatedCodingQuestion);
        updatedCodingQuestion
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .maxScore(UPDATED_MAX_SCORE)
            .starterCode(UPDATED_STARTER_CODE)
            .language(UPDATED_LANGUAGE);
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(updatedCodingQuestion);

        restCodingQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codingQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codingQuestionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCodingQuestionToMatchAllProperties(updatedCodingQuestion);
    }

    @Test
    @Transactional
    void putNonExistingCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, codingQuestionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codingQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(codingQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCodingQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codingQuestion using partial update
        CodingQuestion partialUpdatedCodingQuestion = new CodingQuestion();
        partialUpdatedCodingQuestion.setId(codingQuestion.getId());

        partialUpdatedCodingQuestion.title(UPDATED_TITLE).starterCode(UPDATED_STARTER_CODE).language(UPDATED_LANGUAGE);

        restCodingQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodingQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodingQuestion))
            )
            .andExpect(status().isOk());

        // Validate the CodingQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodingQuestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCodingQuestion, codingQuestion),
            getPersistedCodingQuestion(codingQuestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateCodingQuestionWithPatch() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the codingQuestion using partial update
        CodingQuestion partialUpdatedCodingQuestion = new CodingQuestion();
        partialUpdatedCodingQuestion.setId(codingQuestion.getId());

        partialUpdatedCodingQuestion
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .maxScore(UPDATED_MAX_SCORE)
            .starterCode(UPDATED_STARTER_CODE)
            .language(UPDATED_LANGUAGE);

        restCodingQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCodingQuestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCodingQuestion))
            )
            .andExpect(status().isOk());

        // Validate the CodingQuestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCodingQuestionUpdatableFieldsEquals(partialUpdatedCodingQuestion, getPersistedCodingQuestion(partialUpdatedCodingQuestion));
    }

    @Test
    @Transactional
    void patchNonExistingCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, codingQuestionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codingQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(codingQuestionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCodingQuestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        codingQuestion.setId(longCount.incrementAndGet());

        // Create the CodingQuestion
        CodingQuestionDTO codingQuestionDTO = codingQuestionMapper.toDto(codingQuestion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCodingQuestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(codingQuestionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CodingQuestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCodingQuestion() throws Exception {
        // Initialize the database
        insertedCodingQuestion = codingQuestionRepository.saveAndFlush(codingQuestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the codingQuestion
        restCodingQuestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, codingQuestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return codingQuestionRepository.count();
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

    protected CodingQuestion getPersistedCodingQuestion(CodingQuestion codingQuestion) {
        return codingQuestionRepository.findById(codingQuestion.getId()).orElseThrow();
    }

    protected void assertPersistedCodingQuestionToMatchAllProperties(CodingQuestion expectedCodingQuestion) {
        assertCodingQuestionAllPropertiesEquals(expectedCodingQuestion, getPersistedCodingQuestion(expectedCodingQuestion));
    }

    protected void assertPersistedCodingQuestionToMatchUpdatableProperties(CodingQuestion expectedCodingQuestion) {
        assertCodingQuestionAllUpdatablePropertiesEquals(expectedCodingQuestion, getPersistedCodingQuestion(expectedCodingQuestion));
    }
}
