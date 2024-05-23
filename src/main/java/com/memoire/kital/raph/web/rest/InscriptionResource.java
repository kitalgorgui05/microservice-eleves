package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.restClient.ClasseClient;
import com.memoire.kital.raph.service.InscriptionService;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.web.rest.errors.BadRequestAlertException;
import com.memoire.kital.raph.service.dto.InscriptionDTO;
import com.memoire.kital.raph.service.dto.InscriptionCriteria;
import com.memoire.kital.raph.service.InscriptionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.memoire.kital.raph.domain.Inscription}.
 */
@RestController
@RequestMapping("/api")
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);

    private static final String ENTITY_NAME = "inscriptiondbInscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InscriptionService inscriptionService;

    private final InscriptionQueryService inscriptionQueryService;

    public InscriptionResource(InscriptionService inscriptionService, InscriptionQueryService inscriptionQueryService) {
        this.inscriptionService = inscriptionService;
        this.inscriptionQueryService = inscriptionQueryService;
    }

    @PostMapping("/inscriptions")
    public ResponseEntity<InscriptionDTO> createInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.created(new URI("/api/inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getEleve().getPrenom().toString()+" "+result.getEleve().getNom().toString()))
            .body(result);
    }

    @PutMapping("/inscriptions")
    public ResponseEntity<InscriptionDTO> updateInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inscriptionDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/inscriptions")
    public ResponseEntity<List<InscriptionDTO>> getAllInscriptions(InscriptionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Inscriptions by criteria: {}", criteria);
        Page<InscriptionDTO> page = inscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/inscriptions/count")
    public ResponseEntity<Long> countInscriptions(InscriptionCriteria criteria) {
        log.debug("REST request to count Inscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(inscriptionQueryService.countByCriteria(criteria));
    }

    @GetMapping("/inscriptions/{id}")
    public ResponseEntity<InscriptionDTO> getInscription(@PathVariable String id) {
        log.debug("REST request to get Inscription : {}", id);
        Optional<InscriptionDTO> inscriptionDTO = inscriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inscriptionDTO);
    }

    @DeleteMapping("/inscriptions/{id}")
    public ResponseEntity<Void> deleteInscription(@PathVariable String id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
