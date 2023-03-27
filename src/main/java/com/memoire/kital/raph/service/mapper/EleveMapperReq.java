package com.memoire.kital.raph.service.mapper;


import com.memoire.kital.raph.domain.Eleve;
import com.memoire.kital.raph.service.dto.EleveDTO;
import com.memoire.kital.raph.service.dto.EleveDTOReq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TuteurMapper.class})
public interface EleveMapperReq extends EntityMapper<EleveDTOReq, Eleve> {

    //@Mapping(source = "tuteur.id", target = "tuteurId")
    EleveDTOReq toDto(Eleve eleve);

    //@Mapping(source = "tuteurId", target = "tuteur")
    Eleve toEntity(EleveDTOReq eleveDTO);

    default Eleve fromId(String id) {
        if (id == null) {
            return null;
        }
        Eleve eleve = new Eleve();
        eleve.setId(id);
        return eleve;
    }
}
