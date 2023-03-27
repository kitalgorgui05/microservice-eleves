package com.memoire.kital.raph.service;

import com.memoire.kital.raph.service.dto.TuteurDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.memoire.kital.raph.domain.Tuteur}.
 */
public interface TuteurService {

    /**
     * Save a tuteur.
     *
     * @param tuteurDTO the entity to save.
     * @return the persisted entity.
     */
    TuteurDTO save(TuteurDTO tuteurDTO);

    /**
     * Get all the tuteurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TuteurDTO> findAll(Pageable pageable);


    /**
     * Get the "id" tuteur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TuteurDTO> findOne(String id);

    /**
     * Delete the "id" tuteur.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
