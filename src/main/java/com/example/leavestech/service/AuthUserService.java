package com.example.leavestech.service;

import com.example.leavestech.domain.AuthUser;
import com.example.leavestech.repository.AuthUserRepository;
import com.example.leavestech.service.dto.AuthUserDTO;
import com.example.leavestech.service.mapper.AuthUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.leavestech.domain.AuthUser}.
 */
@Service
@Transactional
public class AuthUserService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthUserService.class);

    private final AuthUserRepository authUserRepository;

    private final AuthUserMapper authUserMapper;

    public AuthUserService(AuthUserRepository authUserRepository, AuthUserMapper authUserMapper) {
        this.authUserRepository = authUserRepository;
        this.authUserMapper = authUserMapper;
    }

    /**
     * Save a authUser.
     *
     * @param authUserDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthUserDTO save(AuthUserDTO authUserDTO) {
        LOG.debug("Request to save AuthUser : {}", authUserDTO);
        AuthUser authUser = authUserMapper.toEntity(authUserDTO);
        authUser = authUserRepository.save(authUser);
        return authUserMapper.toDto(authUser);
    }

    /**
     * Update a authUser.
     *
     * @param authUserDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthUserDTO update(AuthUserDTO authUserDTO) {
        LOG.debug("Request to update AuthUser : {}", authUserDTO);
        AuthUser authUser = authUserMapper.toEntity(authUserDTO);
        authUser = authUserRepository.save(authUser);
        return authUserMapper.toDto(authUser);
    }

    /**
     * Partially update a authUser.
     *
     * @param authUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthUserDTO> partialUpdate(AuthUserDTO authUserDTO) {
        LOG.debug("Request to partially update AuthUser : {}", authUserDTO);

        return authUserRepository
            .findById(authUserDTO.getId())
            .map(existingAuthUser -> {
                authUserMapper.partialUpdate(existingAuthUser, authUserDTO);

                return existingAuthUser;
            })
            .map(authUserRepository::save)
            .map(authUserMapper::toDto);
    }

    /**
     * Get all the authUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthUserDTO> findAll() {
        LOG.debug("Request to get all AuthUsers");
        return authUserRepository.findAll().stream().map(authUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the authUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AuthUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return authUserRepository.findAllWithEagerRelationships(pageable).map(authUserMapper::toDto);
    }

    /**
     *  Get all the authUsers where Student is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthUserDTO> findAllWhereStudentIsNull() {
        LOG.debug("Request to get all authUsers where Student is null");
        return StreamSupport.stream(authUserRepository.findAll().spliterator(), false)
            .filter(authUser -> authUser.getStudent() == null)
            .map(authUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the authUsers where Teacher is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthUserDTO> findAllWhereTeacherIsNull() {
        LOG.debug("Request to get all authUsers where Teacher is null");
        return StreamSupport.stream(authUserRepository.findAll().spliterator(), false)
            .filter(authUser -> authUser.getTeacher() == null)
            .map(authUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one authUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthUserDTO> findOne(Long id) {
        LOG.debug("Request to get AuthUser : {}", id);
        return authUserRepository.findOneWithEagerRelationships(id).map(authUserMapper::toDto);
    }

    /**
     * Delete the authUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AuthUser : {}", id);
        authUserRepository.deleteById(id);
    }
}
