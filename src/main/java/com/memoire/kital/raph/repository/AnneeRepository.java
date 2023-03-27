package com.memoire.kital.raph.repository;

import com.memoire.kital.raph.domain.Annee;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Annee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeRepository extends JpaRepository<Annee, String>, JpaSpecificationExecutor<Annee> {
}
