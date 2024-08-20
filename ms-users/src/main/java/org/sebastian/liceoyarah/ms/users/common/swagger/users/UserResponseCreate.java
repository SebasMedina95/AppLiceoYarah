package org.sebastian.liceoyarah.ms.users.common.swagger.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebastian.liceoyarah.ms.users.entities.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseCreate {

    private User data;
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
