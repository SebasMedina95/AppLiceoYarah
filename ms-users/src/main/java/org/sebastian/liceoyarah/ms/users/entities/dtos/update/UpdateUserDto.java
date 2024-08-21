package org.sebastian.liceoyarah.ms.users.entities.dtos.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDto {

    @NotEmpty(message = "El email de la persona es requerido")
    @Size(min = 3, max = 150, message = "El email de la persona no debe sobrepasar los 150")
    private String email;

}
