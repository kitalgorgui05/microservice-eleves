package com.memoire.kital.raph.service.dto;

import com.memoire.kital.raph.domain.enumeration.Sexe;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@EqualsAndHashCode
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EleveClient {
    private String id;
    private String prenom;
    private String nom;
}
