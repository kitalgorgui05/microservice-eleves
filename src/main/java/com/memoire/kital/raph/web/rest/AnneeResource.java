package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.service.AnneeService;
import com.memoire.kital.raph.web.rest.errors.BadRequestAlertException;
import com.memoire.kital.raph.service.dto.AnneeDTO;
import com.memoire.kital.raph.service.dto.AnneeCriteria;
import com.memoire.kital.raph.service.AnneeQueryService;

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

@RestController
@RequestMapping("/api")
public class AnneeResource {

    private final Logger log = LoggerFactory.getLogger(AnneeResource.class);

    private static final String ENTITY_NAME = "inscriptiondbAnnee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnneeService anneeService;

    private final AnneeQueryService anneeQueryService;

    public AnneeResource(AnneeService anneeService, AnneeQueryService anneeQueryService) {
        this.anneeService = anneeService;
        this.anneeQueryService = anneeQueryService;
    }

    @PostMapping("/annees")
    public ResponseEntity<AnneeDTO> createAnnee(@Valid @RequestBody AnneeDTO anneeDTO) throws URISyntaxException {
        log.debug("REST request to save Annee : {}", anneeDTO);
        if (anneeDTO.getId() != null) {
            throw new BadRequestAlertException("A new annee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnneeDTO result = anneeService.save(anneeDTO);
        return ResponseEntity.created(new URI("/api/annees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/annees")
    public ResponseEntity<AnneeDTO> updateAnnee(@Valid @RequestBody AnneeDTO anneeDTO) throws URISyntaxException {
        log.debug("REST request to update Annee : {}", anneeDTO);
        if (anneeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnneeDTO result = anneeService.save(anneeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, anneeDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/annees")
    public ResponseEntity<List<AnneeDTO>> getAllAnnees(AnneeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Annees by criteria: {}", criteria);
        Page<AnneeDTO> page = anneeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/annees/count")
    public ResponseEntity<Long> countAnnees(AnneeCriteria criteria) {
        log.debug("REST request to count Annees by criteria: {}", criteria);
        return ResponseEntity.ok().body(anneeQueryService.countByCriteria(criteria));
    }

    @GetMapping("/annees/{id}")
    public ResponseEntity<AnneeDTO> getAnnee(@PathVariable String id) {
        log.debug("REST request to get Annee : {}", id);
        Optional<AnneeDTO> anneeDTO = anneeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anneeDTO);
    }

    @DeleteMapping("/annees/{id}")
    public ResponseEntity<Void> deleteAnnee(@PathVariable String id) {
        log.debug("REST request to delete Annee : {}", id);
        anneeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
