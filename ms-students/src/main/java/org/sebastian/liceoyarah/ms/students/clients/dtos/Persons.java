package org.sebastian.liceoyarah.ms.students.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Persons {

    private Long id;
    private String documentType;
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private String email;
    private String gender;
    private String phone1;
    private String phone2;
    private String address;
    private String neighborhood;
    private String description;
    private String civilStatus;
    private LocalDate birthDate;
    private Boolean status;
    private String userCreated;
    private Date dateCreated;
    private String userUpdated;
    private Date dateUpdated;

}
