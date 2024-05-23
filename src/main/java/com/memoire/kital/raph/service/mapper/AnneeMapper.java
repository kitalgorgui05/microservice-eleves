package com.memoire.kital.raph.service.mapper;


import com.memoire.kital.raph.domain.*;
import com.memoire.kital.raph.service.dto.AnneeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annee} and its DTO {@link AnneeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnneeMapper extends EntityMapper<AnneeDTO, Annee> {
    default Annee fromId(String id) {
        if (id == null) {
            return null;
        }
        Annee annee = new Annee();
        annee.setId(id);
        return annee;
    }
}
