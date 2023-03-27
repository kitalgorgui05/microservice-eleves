package com.memoire.kital.raph.repository;

import com.memoire.kital.raph.domain.Eleve;

import com.memoire.kital.raph.domain.Tuteur;
import com.memoire.kital.raph.domain.enumeration.Sexe;
import com.memoire.kital.raph.service.dto.EleveDTOReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Eleve entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EleveRepository extends JpaRepository<Eleve, String>, JpaSpecificationExecutor<Eleve> {
    //la liste des eleves inscrits
    String $sql = "SELECT e.* FROM Inscriptiondb.eleves e JOIN Inscriptiondb.inscriptions i ON e.id = i.eleve_id";
   @Query(value = $sql, nativeQuery = true)
    List<Eleve> getAllEleveByInscription();
}
