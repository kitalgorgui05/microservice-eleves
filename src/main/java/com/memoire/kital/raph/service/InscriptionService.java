package com.memoire.kital.raph.service;

import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.dto.InscriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InscriptionService {
    InscriptionDTO save(InscriptionDTO inscriptionDTO);
    Page<InscriptionDTO> findAll(Pageable pageable);
    Optional<InscriptionDTO> findOne(String id);
    void delete(String id);
}
