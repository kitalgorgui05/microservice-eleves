package com.memoire.kital.raph.service.impl;

import com.memoire.kital.raph.service.TuteurService;
import com.memoire.kital.raph.domain.Tuteur;
import com.memoire.kital.raph.repository.TuteurRepository;
import com.memoire.kital.raph.service.dto.TuteurDTO;
import com.memoire.kital.raph.service.mapper.TuteurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Tuteur}.
 */
@Service
@Transactional
public class TuteurServiceImpl implements TuteurService {

    private final Logger log = LoggerFactory.getLogger(TuteurServiceImpl.class);

    private final TuteurRepository tuteurRepository;

    private final TuteurMapper tuteurMapper;

    public TuteurServiceImpl(TuteurRepository tuteurRepository, TuteurMapper tuteurMapper) {
        this.tuteurRepository = tuteurRepository;
        this.tuteurMapper = tuteurMapper;
    }

    @Override
    public TuteurDTO save(TuteurDTO tuteurDTO) {
        log.debug("Request to save Tuteur : {}", tuteurDTO);
        Tuteur tuteur = tuteurMapper.toEntity(tuteurDTO);
        tuteur = tuteurRepository.save(tuteur);
        return tuteurMapper.toDto(tuteur);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TuteurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tuteurs");
        return tuteurRepository.findAll(pageable)
            .map(tuteurMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<TuteurDTO> findOne(String id) {
        log.debug("Request to get Tuteur : {}", id);
        return tuteurRepository.findById(id)
            .map(tuteurMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Tuteur : {}", id);
        tuteurRepository.deleteById(id);
    }
}
