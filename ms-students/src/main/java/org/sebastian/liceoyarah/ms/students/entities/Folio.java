package org.sebastian.liceoyarah.ms.students.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "TBL_FOLIOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa a los Folios en BD")
@JsonIgnoreProperties({"students"})
public class Folio {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Comment("Clave primaria")
    @Schema(description = "Clave primaria autogenerada")
    private Long id;

    @Column(name = "FOLIO", unique = true, nullable = false, length = 40 )
    @Comment("Código numerativo de Folio")
    @Schema(description = "Código de numeración de Folio")
    @NotNull
    private String numberFolio;

    @Column(name = "RESOLUTION", nullable = false, length = 50 )
    @Comment("Resolución Estatal")
    @Schema(description = "Resolución Estatal")
    @NotNull
    private String resolution;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    @Schema(defaultValue = "true", description = "Estado lógico de eliminación del Folio")
    @NotNull
    private Boolean status;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000 )
    @Comment("Anotaciones adicionales de la persona")
    @Schema(description = "Descripciones adicionales de la Persona")
    private String description;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó al Folio")
    private String userCreated;

    @Column(name = "DATE_CREATED", nullable = true )
    @Comment("Fecha creación")
    @Schema(description = "Fecha creación del Folio")
    private Date dateCreated;

    @Column(name = "USER_UPDATED", nullable = true, length = 100 )
    @Comment("Usuario que actualizó")
    @Schema(defaultValue = "123456789", description = "Usuario que actualizó al Folio")
    private String userUpdated;

    @Column(name = "DATE_UPDATED", nullable = true )
    @Comment("Fecha actualización")
    @Schema(description = "Fecha actualización del Folio")
    private Date dateUpdated;

    @OneToMany(mappedBy = "folio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students;

}
