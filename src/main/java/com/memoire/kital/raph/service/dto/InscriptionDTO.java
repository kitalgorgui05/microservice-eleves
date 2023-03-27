package com.memoire.kital.raph.service.dto;

import java.time.Instant;
import java.io.Serializable;
import com.memoire.kital.raph.domain.enumeration.Etat;
import com.memoire.kital.raph.restClient.ClasseClient;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InscriptionDTO implements Serializable {
    private String id;
    private Instant dateInscription;
    private String classe;
    private ClasseClient classeClient;
    private Boolean transport;
    private Boolean cantine;
    private Etat statut;
    private EleveDTO eleve;
    private AnneeDTO annee;
}
