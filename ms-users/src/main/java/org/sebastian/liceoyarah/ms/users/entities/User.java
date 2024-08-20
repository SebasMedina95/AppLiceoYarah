package org.sebastian.liceoyarah.ms.users.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;

import java.util.Date;

@Entity
@Table(name = "TBL_USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa a los usuarios en BD")
public class User {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    @Schema(description = "Clave primaria autogenerada")
    private Long id;

    @Column(name = "DOCUMENT_PERSON", unique = true, nullable = false, length = 30 )
    @Comment("Número Documento de la persona (Referencia a MS Persons)")
    @Schema(description = "Número de Documento")
    @NotNull
    private String documentNumber;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 100 )
    @Comment("Usuario para cuenta")
    @Schema(description = "Nombre de usuario para cuenta")
    @NotNull
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 100 )
    @Comment("Password de usuario")
    @Schema(description = "Password de usuario")
    @NotNull
    private String password;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 200 )
    @Comment("Email específico para el usuario")
    @Schema(description = "Correo electrónico de específico para el usuario")
    @NotNull
    private String email;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    @Schema(defaultValue = "true", description = "Estado lógico de eliminación del Usuario")
    @NotNull
    private Boolean status;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó al Usuario")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    @Schema(description = "Fecha creación del Usuario")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    @Schema(defaultValue = "123456789", description = "Usuario que actualizó al Usuario")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    @Schema(description = "Fecha actualización del Usuario")
    private Date dateUpdated;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Persons person;

}
