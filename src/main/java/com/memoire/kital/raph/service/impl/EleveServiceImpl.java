package com.memoire.kital.raph.service.impl;

import com.memoire.kital.raph.service.EleveService;
import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.repository.EleveRepository;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.dto.EleveDTOReq;
import com.memoire.kital.raph.service.mapper.EleveMapper;
import com.memoire.kital.raph.service.mapper.EleveMapperReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Eleve}.
 */
@Service
@Transactional
public class EleveServiceImpl implements EleveService {
    private final Logger log = LoggerFactory.getLogger(EleveServiceImpl.class);
    private final EleveRepository eleveRepository;
    private final EleveMapper eleveMapper;
    private final EleveMapperReq eleveMapperReq;

    public EleveServiceImpl(EleveRepository eleveRepository, EleveMapper eleveMapper, EleveMapperReq eleveMapperReq) {
        this.eleveRepository = eleveRepository;
        this.eleveMapper = eleveMapper;
        this.eleveMapperReq = eleveMapperReq;
    }

    @Override
    public EleveDTO save(EleveDTO eleveDTO) {
        log.debug("Request to save Eleve : {}", eleveDTO);
        Eleve eleve = eleveMapper.toEntity(eleveDTO);
        eleve = eleveRepository.save(eleve);
        return eleveMapper.toDto(eleve);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EleveDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Eleves");
        return eleveRepository.findAll(pageable)
            .map(eleveMapper::toDto);
    }


    @Override
    public List<EleveDTOReq> getEleveInscrit() {
        return eleveRepository.getAllEleveByInscription().stream()
            .map(eleve ->eleveMapperReq.toDto(eleve)).collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<EleveDTO> findOne(String id) {
        log.debug("Request to get Eleve : {}", id);
        return eleveRepository.findById(id)
            .map(eleveMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Eleve : {}", id);
        eleveRepository.deleteById(id);
    }
}
