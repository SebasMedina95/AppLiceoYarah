package org.sebastian.liceoyarah.ms.students.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Users;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "TBL_STUDENTS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

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

    @Column(name = "CODE_ISBTTS", unique = true, nullable = false, length = 60 )
    @Comment("Código de aplicabilidad único del estudiante")
    @Schema(description = "Código de aplicabilidad")
    @NotNull
    private String isbttsCode;

    @Column(name = "INSCRIPTION_YEAR", nullable = false, length = 50 )
    @Comment("Año de inscripción")
    @Schema(description = "Año de inscripción")
    @NotNull
    private String yearInscription;

    @Column(name = "SPECIAL_CONDITION" )
    @Comment("Condición especial del estudiante")
    @Schema(defaultValue = "false", description = "Condición especial del estudiante")
    @NotNull
    private Boolean specialCondition;

    @Column(name = "PERSONS_CHARGE", nullable = false, length = 1000 )
    @Comment("Persona(s) responsable(s) del estudiante (Guardar como String JSON)")
    @Schema(description = "Acudiente(s) responsable del estudiante (Guardar como String JSON)")
    @NotNull
    @JsonIgnore  // Oculta el campo personsCharge en la serialización JSON
    private String personsCharge;

    @Column(name = "STATUS" )
    @Comment("Estado eliminación lógica")
    @Schema(defaultValue = "true", description = "Estado lógico de eliminación del Estudiante")
    @NotNull
    private Boolean status;

    @Column(name = "DESCRIPTION", nullable = true, length = 1000 )
    @Comment("Anotaciones adicionales del estudiante")
    @Schema(description = "Descripciones adicionales del Estudiante")
    private String description;

    @Column(name = "USER_CREATED", nullable = true, length = 100 )
    @Comment("Usuario que creó")
    @Schema(defaultValue = "123456789", description = "Usuario que creó el Estudiante")
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
    @JoinColumn(name = "FK_FOLIO")
    @JsonManagedReference
    @Comment("Folio relacionado")
    private Folio folio;

    @Transient //No hace parte directa,no mapeado a la persistencia.
    private Users person;

    @Transient
    @JsonProperty("personsChargeArray")
    public List<PersonsCharge> getPersonsChargeArray() {
        List<PersonsCharge> list = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(personsCharge,
                    new TypeReference<Map<String, String>>() {});

            map.forEach((key, value) -> list.add(new PersonsCharge(key, value)));
        } catch (Exception e) {
            e.printStackTrace(); // Maneja la excepción según sea necesario
        }
        return list;
    }

}


