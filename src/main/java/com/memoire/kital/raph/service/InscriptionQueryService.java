package com.memoire.kital.raph.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import com.memoire.kital.raph.feignRestClient.ClasseRestClient;
import com.memoire.kital.raph.restClient.ClasseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.memoire.kital.raph.domain.Inscription;
import com.memoire.kital.raph.domain.*; // for static metamodels
import com.memoire.kital.raph.repository.InscriptionRepository;
import com.memoire.kital.raph.service.dto.InscriptionCriteria;
import com.memoire.kital.raph.service.dto.InscriptionDTO;
import com.memoire.kital.raph.service.mapper.InscriptionMapper;

@Service
@Transactional(readOnly = true)
public class InscriptionQueryService extends QueryService<Inscription> {

    private final Logger log = LoggerFactory.getLogger(InscriptionQueryService.class);

    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;
    private final ClasseRestClient classeRestClient;

    public InscriptionQueryService(InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper, ClasseRestClient classeRestClient) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
        this.classeRestClient = classeRestClient;
    }


    @Transactional(readOnly = true)
    public List<InscriptionDTO> findByCriteria(InscriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Inscription> specification = createSpecification(criteria);
        return inscriptionMapper.toDto(inscriptionRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public Page<InscriptionDTO> findByCriteria(InscriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Inscription> specification = createSpecification(criteria);
        Page<Inscription> inscriptionPage=inscriptionRepository.findAll(specification, page);
        for (Inscription inscription:inscriptionPage.getContent()){
            ClasseClient classeClient=classeRestClient.getClasse(inscription.getClasse()).getBody();
            inscription.setClasseClient(classeClient);
        }
        return inscriptionPage.map(inscriptionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(InscriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Inscription> specification = createSpecification(criteria);
        return inscriptionRepository.count(specification);
    }

    protected Specification<Inscription> createSpecification(InscriptionCriteria criteria) {
        Specification<Inscription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(), Inscription_.id));
            }
            if (criteria.getDateInscription() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateInscription(), Inscription_.dateInscription));
            }
            if (criteria.getClasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClasse(), Inscription_.classe));
            }
            if (criteria.getTransport() != null) {
                specification = specification.and(buildSpecification(criteria.getTransport(), Inscription_.transport));
            }
            if (criteria.getCantine() != null) {
                specification = specification.and(buildSpecification(criteria.getCantine(), Inscription_.cantine));
            }
            if (criteria.getStatut() != null) {
                specification = specification.and(buildSpecification(criteria.getStatut(), Inscription_.statut));
            }
            if (criteria.getEleveId() != null) {
                specification = specification.and(buildSpecification(criteria.getEleveId(),
                    root -> root.join(Inscription_.eleve, JoinType.LEFT).get(Eleve_.id)));
            }
            if (criteria.getAnneeId() != null) {
                specification = specification.and(buildSpecification(criteria.getAnneeId(),
                    root -> root.join(Inscription_.annee, JoinType.LEFT).get(Annee_.id)));
            }
        }
        return specification;
    }
}
