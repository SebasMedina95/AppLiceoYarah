package org.sebastian.liceoyarah.ms.persons.entities.dtos.update;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePersonDto {

    @NotEmpty(message = "El primer nombre de la persona es requerido")
    @Size(min = 3, max = 50, message = "El primer nombre de la persona debe ser mínimo de 3 caracteres y máximo de 50")
    private String firstName;

    private String secondName;

    @NotEmpty(message = "El primer apellido de la persona es requerido")
    @Size(min = 3, max = 50, message = "El primer apellido de la persona debe ser mínimo de 3 caracteres y máximo de 50")
    private String firstSurname;

    private String secondSurname;

    @NotEmpty(message = "El email de la persona es requerido")
    @Size(min = 3, max = 150, message = "El email de la persona no debe sobrepasar los 150")
    private String email;

    @NotEmpty(message = "El género de la persona es requerido")
    @Size(min = 1, max = 1, message = "El género de la persona no debe sobrepasar los 1")
    private String gender;

    @NotEmpty(message = "El primer número contacto de la persona es requerido")
    @Size(min = 7, max = 20, message = "El primer numero contacto de la persona debe ser mínimo de 7 caracteres y máximo de 20")
    private String phone1;

    private String phone2;

    @NotEmpty(message = "La dirección de la persona es requerido")
    @Size(min = 5, max = 150, message = "La dirección de la persona debe ser mínimo de 5 caracteres y máximo de 150")
    private String address;

    @NotEmpty(message = "El barrio de residencia de la persona es requerido")
    @Size(min = 5, max = 250, message = "El barrio de residencia de la persona debe ser mínimo de 5 caracteres y máximo de 250")
    private String neighborhood;

    private String description;

    @NotEmpty(message = "El estado civil de la persona es requerido")
    @Size(min = 5, max = 30, message = "El estado civil de la persona debe ser mínimo de 5 caracteres y máximo de 50")
    private String civilStatus;

    @NotNull(message = "La fecha de nacimiento es requerido")
    private LocalDate birthDate;

}
