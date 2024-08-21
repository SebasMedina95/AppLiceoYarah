package org.sebastian.liceoyarah.ms.users.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.liceoyarah.ms.users.common.dtos.PaginationDto;
import org.sebastian.liceoyarah.ms.users.common.swagger.users.*;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.sebastian.liceoyarah.ms.users.common.utils.CustomPagedResourcesAssembler;
import org.sebastian.liceoyarah.ms.users.common.utils.ErrorsValidationsResponse;
import org.sebastian.liceoyarah.ms.users.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.users.entities.User;
import org.sebastian.liceoyarah.ms.users.entities.dtos.create.CreateUserDto;
import org.sebastian.liceoyarah.ms.users.services.UserService;
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
@RequestMapping("/v1/api/users")
@Tag(name = "Controlador de Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UserController {

    private final UserService userService;
    private final CustomPagedResourcesAssembler<User> customPagedResourcesAssembler;

    @Autowired
    public UserController(
            UserService userService,
            CustomPagedResourcesAssembler<User> customPagedResourcesAssembler
    ){
        this.userService = userService;
        this.customPagedResourcesAssembler = customPagedResourcesAssembler;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un Usuario", description = "Creación de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario Registrado Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseCreate.class))),
            @ApiResponse(responseCode = "406", description = "Errores en los campos de creación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseCreateErrorFields.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error, incluyendo: " +
                    "La persona ya se encuentra asociada como usuario.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> create(
            @Valid
            @RequestBody CreateUserDto userRequest,
            BindingResult result
    ){

        //? Validación de campos
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
        ResponseWrapper<User> userNew = userService.create(userRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( userNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseConsolidation<>(
                            userNew.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Usuario Registrado Correctamente.",
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
                                userNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PostMapping("/find-all")
    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Obtener todos los usuarios con paginación y también aplicando filtros",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para la paginación y búsqueda",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaginationDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de deportistas.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseList.class))),
            @ApiResponse(responseCode = "400", description = "Errores en los campos de paginación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseListError.class)))
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(
            @Valid
            @RequestBody PaginationDto paginationDto,
            BindingResult result
    ){

        if (paginationDto.getPage() < 1) paginationDto.setPage(1); //Para controlar la página 0, y que la paginación arranque en 1.

        Sort.Direction direction = paginationDto.getOrder().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(paginationDto.getPage() - 1, paginationDto.getSize(), Sort.by(direction, paginationDto.getSort())); //Generando el esquema de paginación para aplicar y ordenamiento
        Page<User> users = userService.findAll(paginationDto.getSearch(), pageable); //Aplicando la paginación JPA -> Incorporo el buscador
        UriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri(); //Para la obtención de la URL

        PagedModel<User> pagedModel = customPagedResourcesAssembler.toModel(users, uriBuilder);

        return ResponseEntity.ok(new ApiResponseConsolidation<>(
                pagedModel,
                new ApiResponseConsolidation.Meta(
                        "Listado de usuarios.",
                        HttpStatus.OK.value(),
                        LocalDateTime.now()
                )
        ));

    }

}
