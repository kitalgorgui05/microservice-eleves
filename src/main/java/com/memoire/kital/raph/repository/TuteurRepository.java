package com.memoire.kital.raph.repository;

import com.memoire.kital.raph.domain.Tuteur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Tuteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TuteurRepository extends JpaRepository<Tuteur, String>, JpaSpecificationExecutor<Tuteur> {
}
