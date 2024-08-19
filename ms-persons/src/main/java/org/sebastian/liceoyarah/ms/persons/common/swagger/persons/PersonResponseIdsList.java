package org.sebastian.liceoyarah.ms.persons.common.swagger.persons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponseIdsList {

    private List<Long> data;
    private PersonResponseCreate.Meta meta;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meta {

        private String message;
        private int code;
        private LocalDateTime date;

    }

}
