package com.memoire.kital.raph.service.impl;

import com.memoire.kital.raph.service.AnneeService;
import com.memoire.kital.raph.domain.Annee;
import com.memoire.kital.raph.repository.AnneeRepository;
import com.memoire.kital.raph.service.dto.AnneeDTO;
import com.memoire.kital.raph.service.mapper.AnneeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AnneeServiceImpl implements AnneeService {
    private final Logger log = LoggerFactory.getLogger(AnneeServiceImpl.class);
    private final AnneeRepository anneeRepository;
    private final AnneeMapper anneeMapper;

    public AnneeServiceImpl(AnneeRepository anneeRepository, AnneeMapper anneeMapper) {
        this.anneeRepository = anneeRepository;
        this.anneeMapper = anneeMapper;
    }

    @Override
    public AnneeDTO save(AnneeDTO anneeDTO) {
        log.debug("Request to save Annee : {}", anneeDTO);
        Annee annee = anneeMapper.toEntity(anneeDTO);
        annee = anneeRepository.save(annee);
        return anneeMapper.toDto(annee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnneeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Annees");
        return anneeRepository.findAll(pageable)
            .map(anneeMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeDTO> findOne(String id) {
        log.debug("Request to get Annee : {}", id);
        return anneeRepository.findById(id)
            .map(anneeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Annee : {}", id);
        anneeRepository.deleteById(id);
    }
}
