package org.sebastian.liceoyarah.ms.students.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.liceoyarah.ms.students.common.dtos.PaginationDto;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.sebastian.liceoyarah.ms.students.common.utils.CustomPagedResourcesAssembler;
import org.sebastian.liceoyarah.ms.students.common.utils.ErrorsValidationsResponse;
import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateFolioDto;
import org.sebastian.liceoyarah.ms.students.services.FolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/api/folios")
@Tag(
        name = "Controlador de MicroServicio Estudiantes - Folio",
        description = "Operaciones relacionadas con el micro servicio de estudiantes - Folios"
)
public class FolioController {

    private final FolioService folioService;
    private final CustomPagedResourcesAssembler<Folio> customPagedResourcesAssembler;

    @Autowired
    public FolioController(
            FolioService folioService,
            CustomPagedResourcesAssembler<Folio> customPagedResourcesAssembler
    ){
        this.folioService = folioService;
        this.customPagedResourcesAssembler = customPagedResourcesAssembler;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponseConsolidation<Object>> create(
            @Valid
            @RequestBody CreateFolioDto folioRequest,
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
        ResponseWrapper<Folio> personNew = folioService.create(folioRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( personNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseConsolidation<>(
                            personNew.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Folio Registrado Correctamente.",
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
                                personNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PostMapping("/find-all")
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(
            @Valid @RequestBody PaginationDto paginationDto,
            BindingResult result
    ){

        //Validación de campos
        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
                                    "Errores en los campos de paginación",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if (paginationDto.getPage() < 1) paginationDto.setPage(1); //Para controlar la página 0, y que la paginación arranque en 1.

        Sort.Direction direction = paginationDto.getOrder().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getSize(), Sort.by(direction, paginationDto.getSort())); //Generando el esquema de paginación para aplicar y ordenamiento
        Page<Folio> folios = folioService.findAll(paginationDto.getSearch(), pageable); //Aplicando la paginación JPA -> Incorporo el buscador
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri(); //Para la obtención de la URL

        PagedModel<Folio> pagedModel = customPagedResourcesAssembler.toModel(folios, uriBuilder);

        return ResponseEntity.ok(new ApiResponseConsolidation<>(
                pagedModel,
                new ApiResponseConsolidation.Meta(
                        "Listado de folios.",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        ));

    }

}
