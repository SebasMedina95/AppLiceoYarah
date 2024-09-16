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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
