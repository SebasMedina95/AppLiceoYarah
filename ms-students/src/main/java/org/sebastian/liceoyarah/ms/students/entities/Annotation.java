package org.sebastian.liceoyarah.ms.students.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Users;

import java.util.Date;

@Entity
@Table(name = "TBL_ANNOTATIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa las anotaciones a estudiantes en BD")
public class Annotation {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    @Schema(description = "Clave primaria autogenerada")
    private Long id;

    @Column(name = "DOCUMENT_USER", unique = true, nullable = false, length = 30 )
    @Comment("Número Documento del usuario/persona (Referencia a MS Users)")
    @Schema(description = "Número de Documento de profesor que imparte infracción")
    @NotNull
    private String documentNumberProfessor;

    @Column(name = "ANNOTATION", nullable = false, length = 5000 )
    @Comment("Anotación infractoria del estudiante")
    @Schema(description = "Anotación infractoria al Estudiante")
    private String annotationInfraction;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó la anotación")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    @Schema(description = "Fecha creación del Estudiante")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    @Schema(defaultValue = "123456789", description = "Usuario que actualizó el Estudiante")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    @Schema(description = "Fecha actualización del Estudiante")
    private Date dateUpdated;

    @ManyToOne
    @JoinColumn(name = "FK_STUDENT")
    @JsonManagedReference
    @Comment("Estudiante relacionado")
    private Student student;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Users professor;

}
