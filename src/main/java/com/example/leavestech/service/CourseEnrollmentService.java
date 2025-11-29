package com.example.leavestech.service;

import com.example.leavestech.domain.CourseEnrollment;
import com.example.leavestech.repository.CourseEnrollmentRepository;
import com.example.leavestech.service.dto.CourseEnrollmentDTO;
import com.example.leavestech.service.mapper.CourseEnrollmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.leavestech.domain.CourseEnrollment}.
 */
@Service
@Transactional
public class CourseEnrollmentService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseEnrollmentService.class);

    private final CourseEnrollmentRepository courseEnrollmentRepository;

    private final CourseEnrollmentMapper courseEnrollmentMapper;

    public CourseEnrollmentService(CourseEnrollmentRepository courseEnrollmentRepository, CourseEnrollmentMapper courseEnrollmentMapper) {
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseEnrollmentMapper = courseEnrollmentMapper;
    }

    /**
     * Save a courseEnrollment.
     *
     * @param courseEnrollmentDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseEnrollmentDTO save(CourseEnrollmentDTO courseEnrollmentDTO) {
        LOG.debug("Request to save CourseEnrollment : {}", courseEnrollmentDTO);
        CourseEnrollment courseEnrollment = courseEnrollmentMapper.toEntity(courseEnrollmentDTO);
        courseEnrollment = courseEnrollmentRepository.save(courseEnrollment);
        return courseEnrollmentMapper.toDto(courseEnrollment);
    }

    /**
     * Update a courseEnrollment.
     *
     * @param courseEnrollmentDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseEnrollmentDTO update(CourseEnrollmentDTO courseEnrollmentDTO) {
        LOG.debug("Request to update CourseEnrollment : {}", courseEnrollmentDTO);
        CourseEnrollment courseEnrollment = courseEnrollmentMapper.toEntity(courseEnrollmentDTO);
        courseEnrollment = courseEnrollmentRepository.save(courseEnrollment);
        return courseEnrollmentMapper.toDto(courseEnrollment);
    }

    /**
     * Partially update a courseEnrollment.
     *
     * @param courseEnrollmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourseEnrollmentDTO> partialUpdate(CourseEnrollmentDTO courseEnrollmentDTO) {
        LOG.debug("Request to partially update CourseEnrollment : {}", courseEnrollmentDTO);

        return courseEnrollmentRepository
            .findById(courseEnrollmentDTO.getId())
            .map(existingCourseEnrollment -> {
                courseEnrollmentMapper.partialUpdate(existingCourseEnrollment, courseEnrollmentDTO);

                return existingCourseEnrollment;
            })
            .map(courseEnrollmentRepository::save)
            .map(courseEnrollmentMapper::toDto);
    }

    /**
     * Get all the courseEnrollments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourseEnrollmentDTO> findAll() {
        LOG.debug("Request to get all CourseEnrollments");
        return courseEnrollmentRepository
            .findAll()
            .stream()
            .map(courseEnrollmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one courseEnrollment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourseEnrollmentDTO> findOne(Long id) {
        LOG.debug("Request to get CourseEnrollment : {}", id);
        return courseEnrollmentRepository.findById(id).map(courseEnrollmentMapper::toDto);
    }

    /**
     * Delete the courseEnrollment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CourseEnrollment : {}", id);
        courseEnrollmentRepository.deleteById(id);
    }
}
