package com.memoire.kital.raph.web.rest;

import com.memoire.kital.raph.service.EleveService;
import com.memoire.kital.raph.service.dto.EleveDTOReq;
import com.memoire.kital.raph.web.rest.errors.BadRequestAlertException;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.dto.EleveCriteria;
import com.memoire.kital.raph.service.EleveQueryService;

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
public class EleveResource {
    private final Logger log = LoggerFactory.getLogger(EleveResource.class);
    private static final String ENTITY_NAME = "inscriptiondbEleve";
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final EleveService eleveService;
    private final EleveQueryService eleveQueryService;
    public EleveResource(EleveService eleveService, EleveQueryService eleveQueryService) {
        this.eleveService = eleveService;
        this.eleveQueryService = eleveQueryService;
    }

    @PostMapping("/eleves")
    public ResponseEntity<EleveDTO> createEleve(@Valid @RequestBody EleveDTO eleveDTO) throws URISyntaxException {
        log.debug("REST request to save Eleve : {}", eleveDTO);
        if (eleveDTO.getId() != null) {
            throw new BadRequestAlertException("A new eleve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EleveDTO result = eleveService.save(eleveDTO);
        return ResponseEntity.created(new URI("/api/eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    @PutMapping("/eleves")
    public ResponseEntity<EleveDTO> updateEleve(@Valid @RequestBody EleveDTO eleveDTO) throws URISyntaxException {
        log.debug("REST request to update Eleve : {}", eleveDTO);
        if (eleveDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EleveDTO result = eleveService.save(eleveDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eleveDTO.getId().toString()))
            .body(result);
    }
    @GetMapping("/eleves")
    public ResponseEntity<List<EleveDTO>> getAllEleves(EleveCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Eleves by criteria: {}", criteria);
        Page<EleveDTO> page = eleveQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    //Liste des eleves inscrits
    @GetMapping("/eleves/inscriptions")
    public List<EleveDTOReq> getAllElevesInscrit(){
        return eleveService.getEleveInscrit();
    }

    @GetMapping("/eleves/count")
    public ResponseEntity<Long> countEleves(EleveCriteria criteria) {
        log.debug("REST request to count Eleves by criteria: {}", criteria);
        return ResponseEntity.ok().body(eleveQueryService.countByCriteria(criteria));
    }
    @GetMapping("/eleves/{id}")
    public ResponseEntity<EleveDTO> getEleve(@PathVariable String id) {
        log.debug("REST request to get Eleve : {}", id);
        Optional<EleveDTO> eleveDTO = eleveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eleveDTO);
    }
    @DeleteMapping("/eleves/{id}")
    public ResponseEntity<Void> deleteEleve(@PathVariable String id) {
        log.debug("REST request to delete Eleve : {}", id);
        eleveService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
