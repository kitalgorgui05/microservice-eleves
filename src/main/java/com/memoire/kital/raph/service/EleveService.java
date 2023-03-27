package com.memoire.kital.raph.service;

import com.memoire.kital.raph.service.dto.EleveDTO;

import com.memoire.kital.raph.service.dto.EleveDTOReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.memoire.kital.raph.domain.Eleve}.
 */
public interface EleveService {
    /**
     * Save a eleve.
     *
     * @param eleveDTO the entity to save.
     * @return the persisted entity.
     */
    EleveDTO save(EleveDTO eleveDTO);

    /**
     * Get all the eleves.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EleveDTO> findAll(Pageable pageable);

    List<EleveDTOReq> getEleveInscrit();

    /**
     * Get the "id" eleve.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EleveDTO> findOne(String id);

    /**
     * Delete the "id" eleve.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
