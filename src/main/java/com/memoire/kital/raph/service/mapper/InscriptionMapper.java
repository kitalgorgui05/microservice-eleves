package com.memoire.kital.raph.service.mapper;


import com.memoire.kital.raph.domain.*;
import com.memoire.kital.raph.service.dto.InscriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inscription} and its DTO {@link InscriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {EleveMapper.class, AnneeMapper.class})
public interface InscriptionMapper extends EntityMapper<InscriptionDTO, Inscription> {

    //@Mapping(source = "eleve.id", target = "eleveId")
   // @Mapping(source = "annee.id", target = "anneeId")
    InscriptionDTO toDto(Inscription inscription);

   // @Mapping(source = "eleveId", target = "eleve")
    //@Mapping(source = "anneeId", target = "annee")
    Inscription toEntity(InscriptionDTO inscriptionDTO);

    default Inscription fromId(String id) {
        if (id == null) {
            return null;
        }
        Inscription inscription = new Inscription();
        inscription.setId(id);
        return inscription;
    }
}
