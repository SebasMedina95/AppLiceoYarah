package com.sebastian.liceoyarah.ms.professors.controllers;

import com.sebastian.liceoyarah.ms.professors.common.dtos.PaginationDto;
import com.sebastian.liceoyarah.ms.professors.common.swagger.professors.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

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

    @PostMapping("/find-all")
    @Operation(
            summary = "Obtener todos los profesores",
            description = "Obtener todos los profesores con paginación y también aplicando filtros",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para la paginación y búsqueda",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginationDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de profesores.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfessorResponseList.class))),
            @ApiResponse(responseCode = "400", description = "Errores en los campos de paginación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfessorResponseListError.class)))
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(
            @Valid
            @RequestBody PaginationDto paginationDto,
            BindingResult result
    ){

        if (paginationDto.getPage() < 1) paginationDto.setPage(1); //Para controlar la página 0, y que la paginación arranque en 1.

        Sort.Direction direction = paginationDto.getOrder().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getSize(), Sort.by(direction, paginationDto.getSort())); //Generando el esquema de paginación para aplicar y ordenamiento
        Page<Professor> professor = professorService.findAll(paginationDto.getSearch(), pageable); //Aplicando la paginación JPA -> Incorporo el buscador
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri(); //Para la obtención de la URL

        PagedModel<Professor> pagedModel = customPagedResourcesAssembler.toModel(professor, uriBuilder);

        return ResponseEntity.ok(new ApiResponseConsolidation<>(
                pagedModel,
                new ApiResponseConsolidation.Meta(
                        "Listado de profesores.",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        ));

    }

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
