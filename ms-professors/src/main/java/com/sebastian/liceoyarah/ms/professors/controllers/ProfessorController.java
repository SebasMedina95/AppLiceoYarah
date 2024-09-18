package com.sebastian.liceoyarah.ms.professors.controllers;

import com.sebastian.liceoyarah.ms.professors.common.swagger.professors.ProfessorResponseCreate;
import com.sebastian.liceoyarah.ms.professors.common.swagger.professors.ProfessorResponseCreateErrorFields;
import com.sebastian.liceoyarah.ms.professors.common.swagger.professors.ProfessorResponseCreateErrorGeneric;
import com.sebastian.liceoyarah.ms.professors.common.utils.ApiResponseConsolidation;
import com.sebastian.liceoyarah.ms.professors.common.utils.CustomPagedResourcesAssembler;
import com.sebastian.liceoyarah.ms.professors.common.utils.ErrorsValidationsResponse;
import com.sebastian.liceoyarah.ms.professors.common.utils.ResponseWrapper;
import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.create.CreateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.services.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/api/professors")
@Tag(
        name = "Controlador de Profesores",
        description = "Operaciones relacionadas con el micro servicio de profesores - Operaciones de Profesores"
)
public class ProfessorController {

    private final ProfessorService professorService;
    private final CustomPagedResourcesAssembler<Professor> customPagedResourcesAssembler;

    @Autowired
    public ProfessorController(
            ProfessorService professorService,
            CustomPagedResourcesAssembler<Professor> customPagedResourcesAssembler
    ){
        this.professorService = professorService;
        this.customPagedResourcesAssembler = customPagedResourcesAssembler;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un Profesor", description = "Creación de un profesor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profesor Registrado Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfessorResponseCreate.class))),
            @ApiResponse(responseCode = "406", description = "Errores en los campos de creación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfessorResponseCreateErrorFields.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfessorResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> create(
            @Valid
            @RequestBody CreateProfessorDto professorRequest,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
                                    "Errores en los campos de creación",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Intentamos realizar el registro
        ResponseWrapper<Professor> professorNew = professorService.create(professorRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( professorNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseConsolidation<>(
                            professorNew.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Profesor Registrado Correctamente.",
                                    HttpStatus.CREATED.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        //? Estamos en este punto, el registro no fue correcto
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                professorNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    //TODO FindAll

    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Obtener profesor por ID",
            description = "Obtener un profesor dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID del profesor a obtener", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profesor encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfessorResponseCreate.class))),
                    @ApiResponse(responseCode = "404", description = "Profesor no encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfessorResponseCreateErrorGeneric.class))),
                    @ApiResponse(responseCode = "400", description = "Error al realizar la búsqueda",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ProfessorResponseCreateErrorGeneric.class)))
            }
    )
    public ResponseEntity<ApiResponseConsolidation<Object>> findById(
            @PathVariable("id") String id
    ){

        ResponseWrapper<Professor> professor;

        //Validamos que el ID que nos proporcionan por la URL sea válido
        try {
            Long professortId = Long.parseLong(id);
            professor = professorService.findById(professortId);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El ID proporcionado para la búsqueda es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( professor.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            professor.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Profesor obtenido por ID.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                professor.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }


}
