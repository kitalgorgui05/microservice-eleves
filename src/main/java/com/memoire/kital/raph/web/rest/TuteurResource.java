package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.service.TuteurService;
import com.memoire.kital.raph.web.rest.errors.BadRequestAlertException;
import com.memoire.kital.raph.service.dto.TuteurDTO;
import com.memoire.kital.raph.service.dto.TuteurCriteria;
import com.memoire.kital.raph.service.TuteurQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.memoire.kital.raph.domain.Tuteur}.
 */
@RestController
@RequestMapping("/api")
public class TuteurResource {

    private final Logger log = LoggerFactory.getLogger(TuteurResource.class);

    private static final String ENTITY_NAME = "inscriptiondbTuteur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TuteurService tuteurService;

    private final TuteurQueryService tuteurQueryService;

    public TuteurResource(TuteurService tuteurService, TuteurQueryService tuteurQueryService) {
        this.tuteurService = tuteurService;
        this.tuteurQueryService = tuteurQueryService;
    }

    /**
     * {@code POST  /tuteurs} : Create a new tuteur.
     *
     * @param tuteurDTO the tuteurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tuteurDTO, or with status {@code 400 (Bad Request)} if the tuteur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tuteurs")
    public ResponseEntity<TuteurDTO> createTuteur(@Valid @RequestBody TuteurDTO tuteurDTO) throws URISyntaxException {
        log.debug("REST request to save Tuteur : {}", tuteurDTO);
        if (tuteurDTO.getId() != null) {
            throw new BadRequestAlertException("A new tuteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TuteurDTO result = tuteurService.save(tuteurDTO);
        return ResponseEntity.created(new URI("/api/tuteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tuteurs} : Updates an existing tuteur.
     *
     * @param tuteurDTO the tuteurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tuteurDTO,
     * or with status {@code 400 (Bad Request)} if the tuteurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tuteurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tuteurs")
    public ResponseEntity<TuteurDTO> updateTuteur(@Valid @RequestBody TuteurDTO tuteurDTO) throws URISyntaxException {
        log.debug("REST request to update Tuteur : {}", tuteurDTO);
        if (tuteurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TuteurDTO result = tuteurService.save(tuteurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tuteurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tuteurs} : get all the tuteurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tuteurs in body.
     */
    @GetMapping("/tuteurs")
    public ResponseEntity<List<TuteurDTO>> getAllTuteurs(TuteurCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tuteurs by criteria: {}", criteria);
        Page<TuteurDTO> page = tuteurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tuteurs/count} : count all the tuteurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tuteurs/count")
    public ResponseEntity<Long> countTuteurs(TuteurCriteria criteria) {
        log.debug("REST request to count Tuteurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(tuteurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tuteurs/:id} : get the "id" tuteur.
     *
     * @param id the id of the tuteurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tuteurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tuteurs/{id}")
    public ResponseEntity<TuteurDTO> getTuteur(@PathVariable String id) {
        log.debug("REST request to get Tuteur : {}", id);
        Optional<TuteurDTO> tuteurDTO = tuteurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tuteurDTO);
    }

    /**
     * {@code DELETE  /tuteurs/:id} : delete the "id" tuteur.
     *
     * @param id the id of the tuteurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tuteurs/{id}")
    public ResponseEntity<Void> deleteTuteur(@PathVariable String id) {
        log.debug("REST request to delete Tuteur : {}", id);
        tuteurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
