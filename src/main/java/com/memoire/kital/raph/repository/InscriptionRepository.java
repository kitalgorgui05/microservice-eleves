package com.memoire.kital.raph.repository;

import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.domain.Inscription;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Inscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, String>, JpaSpecificationExecutor<Inscription> {
}
