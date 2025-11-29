package com.example.leavestech.web.rest;

import com.example.leavestech.repository.TeacherRepository;
import com.example.leavestech.service.TeacherService;
import com.example.leavestech.service.dto.TeacherDTO;
import com.example.leavestech.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.example.leavestech.domain.Teacher}.
 */
@RestController
@RequestMapping("/api/teachers")
public class TeacherResource {

    private static final Logger LOG = LoggerFactory.getLogger(TeacherResource.class);

    private static final String ENTITY_NAME = "teacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeacherService teacherService;

    private final TeacherRepository teacherRepository;

    public TeacherResource(TeacherService teacherService, TeacherRepository teacherRepository) {
        this.teacherService = teacherService;
        this.teacherRepository = teacherRepository;
    }

    /**
     * {@code POST  /teachers} : Create a new teacher.
     *
     * @param teacherDTO the teacherDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teacherDTO, or with status {@code 400 (Bad Request)} if the teacher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TeacherDTO> createTeacher(@RequestBody TeacherDTO teacherDTO) throws URISyntaxException {
        LOG.debug("REST request to save Teacher : {}", teacherDTO);
        if (teacherDTO.getId() != null) {
            throw new BadRequestAlertException("A new teacher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teacherDTO = teacherService.save(teacherDTO);
        return ResponseEntity.created(new URI("/api/teachers/" + teacherDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teacherDTO.getId().toString()))
            .body(teacherDTO);
    }

    /**
     * {@code PUT  /teachers/:id} : Updates an existing teacher.
     *
     * @param id the id of the teacherDTO to save.
     * @param teacherDTO the teacherDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherDTO,
     * or with status {@code 400 (Bad Request)} if the teacherDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teacherDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherDTO teacherDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Teacher : {}, {}", id, teacherDTO);
        if (teacherDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teacherDTO = teacherService.update(teacherDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacherDTO.getId().toString()))
            .body(teacherDTO);
    }

    /**
     * {@code PATCH  /teachers/:id} : Partial updates given fields of an existing teacher, field will ignore if it is null
     *
     * @param id the id of the teacherDTO to save.
     * @param teacherDTO the teacherDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teacherDTO,
     * or with status {@code 400 (Bad Request)} if the teacherDTO is not valid,
     * or with status {@code 404 (Not Found)} if the teacherDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the teacherDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeacherDTO> partialUpdateTeacher(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TeacherDTO teacherDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Teacher partially : {}, {}", id, teacherDTO);
        if (teacherDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teacherDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teacherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeacherDTO> result = teacherService.partialUpdate(teacherDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teacherDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /teachers} : get all the teachers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachers in body.
     */
    @GetMapping("")
    public List<TeacherDTO> getAllTeachers() {
        LOG.debug("REST request to get all Teachers");
        return teacherService.findAll();
    }

    /**
     * {@code GET  /teachers/:id} : get the "id" teacher.
     *
     * @param id the id of the teacherDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teacherDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacher(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Teacher : {}", id);
        Optional<TeacherDTO> teacherDTO = teacherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teacherDTO);
    }

    /**
     * {@code DELETE  /teachers/:id} : delete the "id" teacher.
     *
     * @param id the id of the teacherDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Teacher : {}", id);
        teacherService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
