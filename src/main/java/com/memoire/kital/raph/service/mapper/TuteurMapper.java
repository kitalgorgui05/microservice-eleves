package com.memoire.kital.raph.service.mapper;


import com.memoire.kital.raph.domain.*;
import com.memoire.kital.raph.service.dto.TuteurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tuteur} and its DTO {@link TuteurDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TuteurMapper extends EntityMapper<TuteurDTO, Tuteur> {

    default Tuteur fromId(String id) {
        if (id == null) {
            return null;
        }
        Tuteur tuteur = new Tuteur();
        tuteur.setId(id);
        return tuteur;
    }
}
