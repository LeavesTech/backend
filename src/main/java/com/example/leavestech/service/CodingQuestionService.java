package com.example.leavestech.service;

import com.example.leavestech.domain.CodingQuestion;
import com.example.leavestech.repository.CodingQuestionRepository;
import com.example.leavestech.service.dto.CodingQuestionDTO;
import com.example.leavestech.service.mapper.CodingQuestionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.leavestech.domain.CodingQuestion}.
 */
@Service
@Transactional
public class CodingQuestionService {

    private static final Logger LOG = LoggerFactory.getLogger(CodingQuestionService.class);

    private final CodingQuestionRepository codingQuestionRepository;

    private final CodingQuestionMapper codingQuestionMapper;

    public CodingQuestionService(CodingQuestionRepository codingQuestionRepository, CodingQuestionMapper codingQuestionMapper) {
        this.codingQuestionRepository = codingQuestionRepository;
        this.codingQuestionMapper = codingQuestionMapper;
    }

    /**
     * Save a codingQuestion.
     *
     * @param codingQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    public CodingQuestionDTO save(CodingQuestionDTO codingQuestionDTO) {
        LOG.debug("Request to save CodingQuestion : {}", codingQuestionDTO);
        CodingQuestion codingQuestion = codingQuestionMapper.toEntity(codingQuestionDTO);
        codingQuestion = codingQuestionRepository.save(codingQuestion);
        return codingQuestionMapper.toDto(codingQuestion);
    }

    /**
     * Update a codingQuestion.
     *
     * @param codingQuestionDTO the entity to save.
     * @return the persisted entity.
     */
    public CodingQuestionDTO update(CodingQuestionDTO codingQuestionDTO) {
        LOG.debug("Request to update CodingQuestion : {}", codingQuestionDTO);
        CodingQuestion codingQuestion = codingQuestionMapper.toEntity(codingQuestionDTO);
        codingQuestion = codingQuestionRepository.save(codingQuestion);
        return codingQuestionMapper.toDto(codingQuestion);
    }

    /**
     * Partially update a codingQuestion.
     *
     * @param codingQuestionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CodingQuestionDTO> partialUpdate(CodingQuestionDTO codingQuestionDTO) {
        LOG.debug("Request to partially update CodingQuestion : {}", codingQuestionDTO);

        return codingQuestionRepository
            .findById(codingQuestionDTO.getId())
            .map(existingCodingQuestion -> {
                codingQuestionMapper.partialUpdate(existingCodingQuestion, codingQuestionDTO);

                return existingCodingQuestion;
            })
            .map(codingQuestionRepository::save)
            .map(codingQuestionMapper::toDto);
    }

    /**
     * Get all the codingQuestions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CodingQuestionDTO> findAll() {
        LOG.debug("Request to get all CodingQuestions");
        return codingQuestionRepository
            .findAll()
            .stream()
            .map(codingQuestionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one codingQuestion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CodingQuestionDTO> findOne(Long id) {
        LOG.debug("Request to get CodingQuestion : {}", id);
        return codingQuestionRepository.findById(id).map(codingQuestionMapper::toDto);
    }

    /**
     * Delete the codingQuestion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CodingQuestion : {}", id);
        codingQuestionRepository.deleteById(id);
    }
}
