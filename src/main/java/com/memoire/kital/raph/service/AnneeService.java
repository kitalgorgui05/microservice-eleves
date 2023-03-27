package com.memoire.kital.raph.service;

import com.memoire.kital.raph.service.dto.AnneeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.memoire.kital.raph.domain.Annee}.
 */
public interface AnneeService {

    /**
     * Save a annee.
     *
     * @param anneeDTO the entity to save.
     * @return the persisted entity.
     */
    AnneeDTO save(AnneeDTO anneeDTO);

    /**
     * Get all the annees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeDTO> findAll(Pageable pageable);


    /**
     * Get the "id" annee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnneeDTO> findOne(String id);

    /**
     * Delete the "id" annee.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
