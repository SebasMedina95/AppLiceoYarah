package com.sebastian.liceoyarah.ms.professors.clients.dtos;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Students {

    private Long id;
    private String documentNumber;
    private String isbttsCode;
    private String yearInscription;
    private Boolean specialCondition;
    private String personsCharge;
    private Boolean status;
    private String description;
    private String userCreated;
    private Date dateCreated;
    private String userUpdated;
    private Date dateUpdated;

    //No es necesario cargar el Folio, así que no lo haremos acá.

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Users user;

}
