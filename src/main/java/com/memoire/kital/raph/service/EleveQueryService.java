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

import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.domain.*; // for static metamodels
import com.memoire.kital.raph.repository.EleveRepository;
import com.memoire.kital.raph.service.dto.EleveCriteria;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.mapper.EleveMapper;

/**
 * Service for executing complex queries for {@link Eleve} entities in the database.
 * The main input is a {@link EleveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EleveDTO} or a {@link Page} of {@link EleveDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EleveQueryService extends QueryService<Eleve> {

    private final Logger log = LoggerFactory.getLogger(EleveQueryService.class);

    private final EleveRepository eleveRepository;

    private final EleveMapper eleveMapper;

    public EleveQueryService(EleveRepository eleveRepository, EleveMapper eleveMapper) {
        this.eleveRepository = eleveRepository;
        this.eleveMapper = eleveMapper;
    }

    /**
     * Return a {@link List} of {@link EleveDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EleveDTO> findByCriteria(EleveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Eleve> specification = createSpecification(criteria);
        return eleveMapper.toDto(eleveRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EleveDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EleveDTO> findByCriteria(EleveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Eleve> specification = createSpecification(criteria);
        return eleveRepository.findAll(specification, page)
            .map(eleveMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EleveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Eleve> specification = createSpecification(criteria);
        return eleveRepository.count(specification);
    }

    /**
     * Function to convert {@link EleveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Eleve> createSpecification(EleveCriteria criteria) {
        Specification<Eleve> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), Eleve_.id));
            }
            if (criteria.getMatricule() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMatricule(), Eleve_.matricule));
            }
            if (criteria.getPrenom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrenom(), Eleve_.prenom));
            }
            if (criteria.getNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNom(), Eleve_.nom));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Eleve_.sexe));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), Eleve_.telephone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Eleve_.email));
            }
            if (criteria.getDateNaissance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateNaissance(), Eleve_.dateNaissance));
            }
            if (criteria.getLieuNaissance() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLieuNaissance(), Eleve_.lieuNaissance));
            }
            if (criteria.getTuteurId() != null) {
                specification = specification.and(buildSpecification(criteria.getTuteurId(),
                    root -> root.join(Eleve_.tuteur, JoinType.LEFT).get(Tuteur_.id)));
            }
        }
        return specification;
    }
}
