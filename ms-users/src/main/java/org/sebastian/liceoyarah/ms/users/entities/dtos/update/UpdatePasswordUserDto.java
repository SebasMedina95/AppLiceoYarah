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
public class UpdatePasswordUserDto {

    @NotEmpty(message = "Contraseña actual es requerido")
    @Size(min = 7, max = 30, message = "La contraseña actual debe ser mínimo de 7 caracteres y máximo de 30")
    private String currentPassword;

    @NotEmpty(message = "La nueva contraseña es requerido")
    @Size(min = 7, max = 30, message = "La nueva contraseba debe ser mínimo de 7 caracteres y máximo de 30")
    private String newPassword;

    @NotEmpty(message = "Contraseña actual es requerido")
    @Size(min = 7, max = 30, message = "La nueva contraseba debe ser mínimo de 7 caracteres y máximo de 30")
    private String confirmNewPassword;

}
