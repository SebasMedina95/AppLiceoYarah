package org.sebastian.liceoyarah.ms.users.entities.dtos.create;

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
public class CreateUserDto {

    @NotEmpty(message = "El numero de documento de la persona es requerido")
    @Size(min = 6, max = 30, message = "El numero de documento de la persona debe ser mínimo de 6 caracteres y máximo de 30")
    private String documentNumber;

    @NotEmpty(message = "El email del usuario es requerido")
    @Size(min = 3, max = 150, message = "El email del usuario no debe sobrepasar los 150")
    private String email;

    //? El usuario y la contraseña serán autogenerados

}
