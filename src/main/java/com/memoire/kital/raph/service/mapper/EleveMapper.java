package com.memoire.kital.raph.service.mapper;


import com.memoire.kital.raph.domain.*;
import com.memoire.kital.raph.service.dto.EleveDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {TuteurMapper.class})
public interface EleveMapper extends EntityMapper<EleveDTO, Eleve> {

    //@Mapping(source = "tuteur.id", target = "tuteurId")
    EleveDTO toDto(Eleve eleve);

    //@Mapping(source = "tuteurId", target = "tuteur")
    Eleve toEntity(EleveDTO eleveDTO);

    default Eleve fromId(String id) {
        if (id == null) {
            return null;
        }
        Eleve eleve = new Eleve();
        eleve.setId(id);
        return eleve;
    }
}
