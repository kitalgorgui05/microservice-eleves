package com.memoire.kital.raph.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.memoire.kital.raph.domain.Tuteur;
import com.memoire.kital.raph.domain.*; // for static metamodels
import com.memoire.kital.raph.repository.TuteurRepository;
import com.memoire.kital.raph.service.dto.TuteurCriteria;
import com.memoire.kital.raph.service.dto.TuteurDTO;
import com.memoire.kital.raph.service.mapper.TuteurMapper;

/**
 * Service for executing complex queries for {@link Tuteur} entities in the database.
 * The main input is a {@link TuteurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TuteurDTO} or a {@link Page} of {@link TuteurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TuteurQueryService extends QueryService<Tuteur> {

    private final Logger log = LoggerFactory.getLogger(TuteurQueryService.class);

    private final TuteurRepository tuteurRepository;

    private final TuteurMapper tuteurMapper;

    public TuteurQueryService(TuteurRepository tuteurRepository, TuteurMapper tuteurMapper) {
        this.tuteurRepository = tuteurRepository;
        this.tuteurMapper = tuteurMapper;
    }

    /**
     * Return a {@link List} of {@link TuteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TuteurDTO> findByCriteria(TuteurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Tuteur> specification = createSpecification(criteria);
        return tuteurMapper.toDto(tuteurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TuteurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TuteurDTO> findByCriteria(TuteurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tuteur> specification = createSpecification(criteria);
        return tuteurRepository.findAll(specification, page)
            .map(tuteurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TuteurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Tuteur> specification = createSpecification(criteria);
        return tuteurRepository.count(specification);
    }

    /**
     * Function to convert {@link TuteurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tuteur> createSpecification(TuteurCriteria criteria) {
        Specification<Tuteur> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), Tuteur_.id));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Tuteur_.prenom));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Tuteur_.nom));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Tuteur_.email));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Tuteur_.sexe));
            }
        }
        return specification;
    }
}
