package com.example.leavestech.web.rest;

import com.example.leavestech.repository.CodingQuestionRepository;
import com.example.leavestech.service.CodingQuestionService;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.example.leavestech.domain.CodingQuestion}.
 */
@RestController
@RequestMapping("/api/coding-questions")
public class CodingQuestionResource {

    private static final Logger LOG = LoggerFactory.getLogger(CodingQuestionResource.class);

    private static final String ENTITY_NAME = "codingQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CodingQuestionService codingQuestionService;

    private final CodingQuestionRepository codingQuestionRepository;

    public CodingQuestionResource(CodingQuestionService codingQuestionService, CodingQuestionRepository codingQuestionRepository) {
        this.codingQuestionService = codingQuestionService;
        this.codingQuestionRepository = codingQuestionRepository;
    }

    /**
     * {@code POST  /coding-questions} : Create a new codingQuestion.
     *
     * @param codingQuestionDTO the codingQuestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new codingQuestionDTO, or with status {@code 400 (Bad Request)} if the codingQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CodingQuestionDTO> createCodingQuestion(@Valid @RequestBody CodingQuestionDTO codingQuestionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CodingQuestion : {}", codingQuestionDTO);
        if (codingQuestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new codingQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        codingQuestionDTO = codingQuestionService.save(codingQuestionDTO);
        return ResponseEntity.created(new URI("/api/coding-questions/" + codingQuestionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, codingQuestionDTO.getId().toString()))
            .body(codingQuestionDTO);
    }

    /**
     * {@code PUT  /coding-questions/:id} : Updates an existing codingQuestion.
     *
     * @param id the id of the codingQuestionDTO to save.
     * @param codingQuestionDTO the codingQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codingQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the codingQuestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the codingQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CodingQuestionDTO> updateCodingQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CodingQuestionDTO codingQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CodingQuestion : {}, {}", id, codingQuestionDTO);
        if (codingQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codingQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codingQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        codingQuestionDTO = codingQuestionService.update(codingQuestionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codingQuestionDTO.getId().toString()))
            .body(codingQuestionDTO);
    }

    /**
     * {@code PATCH  /coding-questions/:id} : Partial updates given fields of an existing codingQuestion, field will ignore if it is null
     *
     * @param id the id of the codingQuestionDTO to save.
     * @param codingQuestionDTO the codingQuestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated codingQuestionDTO,
     * or with status {@code 400 (Bad Request)} if the codingQuestionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the codingQuestionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the codingQuestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CodingQuestionDTO> partialUpdateCodingQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CodingQuestionDTO codingQuestionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CodingQuestion partially : {}, {}", id, codingQuestionDTO);
        if (codingQuestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, codingQuestionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!codingQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CodingQuestionDTO> result = codingQuestionService.partialUpdate(codingQuestionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, codingQuestionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /coding-questions} : get all the codingQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of codingQuestions in body.
     */
    @GetMapping("")
    public List<CodingQuestionDTO> getAllCodingQuestions() {
        LOG.debug("REST request to get all CodingQuestions");
        return codingQuestionService.findAll();
    }

    /**
     * {@code GET  /coding-questions/:id} : get the "id" codingQuestion.
     *
     * @param id the id of the codingQuestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the codingQuestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CodingQuestionDTO> getCodingQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CodingQuestion : {}", id);
        Optional<CodingQuestionDTO> codingQuestionDTO = codingQuestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(codingQuestionDTO);
    }

    /**
     * {@code DELETE  /coding-questions/:id} : delete the "id" codingQuestion.
     *
     * @param id the id of the codingQuestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCodingQuestion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CodingQuestion : {}", id);
        codingQuestionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
