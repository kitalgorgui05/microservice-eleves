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

import com.memoire.kital.raph.domain.Annee;
import com.memoire.kital.raph.domain.*; // for static metamodels
import com.memoire.kital.raph.repository.AnneeRepository;
import com.memoire.kital.raph.service.dto.AnneeCriteria;
import com.memoire.kital.raph.service.dto.AnneeDTO;
import com.memoire.kital.raph.service.mapper.AnneeMapper;

/**
 * Service for executing complex queries for {@link Annee} entities in the database.
 * The main input is a {@link AnneeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnneeDTO} or a {@link Page} of {@link AnneeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnneeQueryService extends QueryService<Annee> {

    private final Logger log = LoggerFactory.getLogger(AnneeQueryService.class);

    private final AnneeRepository anneeRepository;

    private final AnneeMapper anneeMapper;

    public AnneeQueryService(AnneeRepository anneeRepository, AnneeMapper anneeMapper) {
        this.anneeRepository = anneeRepository;
        this.anneeMapper = anneeMapper;
    }

    /**
     * Return a {@link List} of {@link AnneeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnneeDTO> findByCriteria(AnneeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Annee> specification = createSpecification(criteria);
        return anneeMapper.toDto(anneeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AnneeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnneeDTO> findByCriteria(AnneeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Annee> specification = createSpecification(criteria);
        return anneeRepository.findAll(specification, page)
            .map(anneeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnneeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Annee> specification = createSpecification(criteria);
        return anneeRepository.count(specification);
    }

    /**
     * Function to convert {@link AnneeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Annee> createSpecification(AnneeCriteria criteria) {
        Specification<Annee> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), Annee_.id));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Annee_.nom));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Annee_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Annee_.dateFin));
            }
        }
        return specification;
    }
}
