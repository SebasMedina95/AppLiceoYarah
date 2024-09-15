package com.sebastian.liceoyarah.ms.professors.clients.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Persons;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    private Long id;
    private String documentNumber;
    private String username;
    private String password;
    private String email;
    private Boolean status;
    private String userCreated;
    private Date dateCreated;
    private String userUpdated;
    private Date dateUpdated;
    private Persons person;

}
