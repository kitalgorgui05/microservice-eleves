package com.memoire.kital.raph.service.impl;

import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.feignRestClient.ClasseRestClient;
import com.memoire.kital.raph.repository.EleveRepository;
import com.memoire.kital.raph.restClient.ClasseClient;
import com.memoire.kital.raph.service.InscriptionService;
import com.memoire.kital.raph.domain.Inscription;
import com.memoire.kital.raph.repository.InscriptionRepository;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.dto.InscriptionDTO;
import com.memoire.kital.raph.service.mapper.EleveMapper;
import com.memoire.kital.raph.service.mapper.InscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;
    private final EleveRepository eleveRepository;

    private final InscriptionMapper inscriptionMapper;
    private final EleveMapper eleveMapper;
    private final ClasseRestClient classeRestClient;


    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, EleveRepository eleveRepository, InscriptionMapper inscriptionMapper, EleveMapper eleveMapper, ClasseRestClient classeRestClient) {
        this.inscriptionRepository = inscriptionRepository;
        this.eleveRepository = eleveRepository;
        this.inscriptionMapper = inscriptionMapper;
        this.eleveMapper = eleveMapper;
        this.classeRestClient = classeRestClient;
    }

    @Override
    public InscriptionDTO save(InscriptionDTO inscriptionDTO) {
        log.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.saveAndFlush(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InscriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable)
            .map(inscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionDTO> findOne(String id) {
        log.debug("Request to get Inscription : {}", id);
        Inscription dto=inscriptionRepository.findById(id).orElse(null);
        if (dto != null){
            ClasseClient classeClient=classeRestClient.getClasse(dto.getClasse()).getBody();
            dto.setClasseClient(classeClient);
        }

       return Optional.ofNullable(inscriptionMapper.toDto(dto));
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }
}
