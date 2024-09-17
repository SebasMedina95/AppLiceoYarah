package com.sebastian.liceoyarah.ms.professors.entities;

import com.sebastian.liceoyarah.ms.professors.clients.dtos.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
@Table(name = "TBL_PROFESSORS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Professor {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    @Schema(description = "Clave primaria autogenerada")
    private Long id;

    @Column(name = "DOCUMENT_USER", unique = true, nullable = false, length = 30 )
    @Comment("Número Documento del usuario/persona (Referencia a MS Users)")
    @Schema(description = "Número de Documento")
    @NotNull
    private String documentNumber;

    @Column(name = "INSTITUTIONAL_EMAIL", unique = true, nullable = false, length = 150 )
    @Comment("Email institucional para profesor")
    @Schema(description = "Email Institucional")
    @NotNull
    private String institutionalEmail;

    @Column(name = "TYPE", nullable = false, length = 1 )
    @Comment("Tipo de Profesor - 1 Planta o 2 Catedra")
    @Schema(description = "Tipo de Profesor")
    @NotNull
    private String type;

    @Column(name = "TITTLE", nullable = false, length = 100 )
    @Comment("Título profesional asociado")
    @Schema(description = "Título profesional")
    @NotNull
    private String proffesionalTittle;

    @Column(name = "TITTLE_CARD", unique = true ,nullable = false, length = 100 )
    @Comment("Código Tarjeta profesional asociado")
    @Schema(description = "Código Tarjeta profesional")
    @NotNull
    private String cardTittle;

    @Column(name = "GROUP_DIRECTOR" )
    @Comment("¿Es director de grupo?")
    @Schema(defaultValue = "false", description = "¿Director de Grupo?")
    @NotNull
    private Boolean groupDirector;

    @Column(name = "TECHNICAL_PROFESSOR" )
    @Comment("¿Es profesor de técnicas profesionales?")
    @Schema(defaultValue = "false", description = "¿Profesor Técnico?")
    @NotNull
    private Boolean technicalProfessor;

    @Column(name = "CORE", nullable = false, length = 1 )
    @Comment("Core de enseñanza - 1 Primaria, 2 Bachillerato, 3 Media Técnica, 4 Mixto")
    @Schema(description = "Core enseñanza")
    @NotNull
    private String core;

    @Column(name = "LABOR_DAY", nullable = false, length = 1 )
    @Comment("Jornada Laboral")
    @Schema(description = "Jornada laboral 1 Completa, 2 Mañana, 3 Tarde, 4 Nocturna, 5 Combinados")
    @NotNull
    private String laborDay;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    @Schema(defaultValue = "true", description = "Estado lógico de eliminación del Profesor")
    @NotNull
    private Boolean status;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000 )
    @Comment("Anotaciones adicionales del profesor")
    @Schema(description = "Descripciones adicionales del profesor")
    private String description;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó el Profesor")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    @Schema(description = "Fecha creación del Profesor")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    @Schema(defaultValue = "123456789", description = "Usuario que actualizó el Profesor")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    @Schema(description = "Fecha actualización del Profesor")
    private Date dateUpdated;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Users user;

}
