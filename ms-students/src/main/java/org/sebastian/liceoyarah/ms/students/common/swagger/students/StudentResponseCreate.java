package org.sebastian.liceoyarah.ms.students.common.swagger.students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastian.liceoyarah.ms.students.entities.Student;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseCreate {

    private Student data;
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
