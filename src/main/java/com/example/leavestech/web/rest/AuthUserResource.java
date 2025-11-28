package com.example.leavestech.web.rest;

import com.example.leavestech.repository.AuthUserRepository;
import com.example.leavestech.service.AuthUserService;
import com.example.leavestech.service.dto.AuthUserDTO;
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
 * REST controller for managing {@link com.example.leavestech.domain.AuthUser}.
 */
@RestController
@RequestMapping("/api/auth-users")
public class AuthUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(AuthUserResource.class);

    private static final String ENTITY_NAME = "authUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthUserService authUserService;

    private final AuthUserRepository authUserRepository;

    public AuthUserResource(AuthUserService authUserService, AuthUserRepository authUserRepository) {
        this.authUserService = authUserService;
        this.authUserRepository = authUserRepository;
    }

    /**
     * {@code POST  /auth-users} : Create a new authUser.
     *
     * @param authUserDTO the authUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authUserDTO, or with status {@code 400 (Bad Request)} if the authUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AuthUserDTO> createAuthUser(@Valid @RequestBody AuthUserDTO authUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save AuthUser : {}", authUserDTO);
        if (authUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new authUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        authUserDTO = authUserService.save(authUserDTO);
        return ResponseEntity.created(new URI("/api/auth-users/" + authUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, authUserDTO.getId().toString()))
            .body(authUserDTO);
    }

    /**
     * {@code PUT  /auth-users/:id} : Updates an existing authUser.
     *
     * @param id the id of the authUserDTO to save.
     * @param authUserDTO the authUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authUserDTO,
     * or with status {@code 400 (Bad Request)} if the authUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthUserDTO> updateAuthUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AuthUserDTO authUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AuthUser : {}, {}", id, authUserDTO);
        if (authUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        authUserDTO = authUserService.update(authUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authUserDTO.getId().toString()))
            .body(authUserDTO);
    }

    /**
     * {@code PATCH  /auth-users/:id} : Partial updates given fields of an existing authUser, field will ignore if it is null
     *
     * @param id the id of the authUserDTO to save.
     * @param authUserDTO the authUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authUserDTO,
     * or with status {@code 400 (Bad Request)} if the authUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the authUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the authUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AuthUserDTO> partialUpdateAuthUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AuthUserDTO authUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AuthUser partially : {}, {}", id, authUserDTO);
        if (authUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AuthUserDTO> result = authUserService.partialUpdate(authUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /auth-users} : get all the authUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authUsers in body.
     */
    @GetMapping("")
    public List<AuthUserDTO> getAllAuthUsers(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("student-is-null".equals(filter)) {
            LOG.debug("REST request to get all AuthUsers where student is null");
            return authUserService.findAllWhereStudentIsNull();
        }

        if ("teacher-is-null".equals(filter)) {
            LOG.debug("REST request to get all AuthUsers where teacher is null");
            return authUserService.findAllWhereTeacherIsNull();
        }
        LOG.debug("REST request to get all AuthUsers");
        return authUserService.findAll();
    }

    /**
     * {@code GET  /auth-users/:id} : get the "id" authUser.
     *
     * @param id the id of the authUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthUserDTO> getAuthUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AuthUser : {}", id);
        Optional<AuthUserDTO> authUserDTO = authUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authUserDTO);
    }

    /**
     * {@code DELETE  /auth-users/:id} : delete the "id" authUser.
     *
     * @param id the id of the authUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AuthUser : {}", id);
        authUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
