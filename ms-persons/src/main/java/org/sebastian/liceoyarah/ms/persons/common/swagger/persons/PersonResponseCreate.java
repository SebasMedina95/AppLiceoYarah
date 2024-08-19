package org.sebastian.liceoyarah.ms.persons.common.swagger.persons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastian.propoligas.persons.persons.entities.PersonEntity;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseCreate {

    private PersonEntity data;
    private Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private String message;
        private int code;
        private LocalDateTime date;

    }

}
