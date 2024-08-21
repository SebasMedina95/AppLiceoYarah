package org.sebastian.liceoyarah.ms.users.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.sebastian.liceoyarah.ms.users.entities.dtos.update.UpdateUserDto;
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
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Obtener usuario por ID",
            description = "Obtener un usuario dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID del usuario a obtener", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseCreate.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseCreateErrorGeneric.class))),
                    @ApiResponse(responseCode = "400", description = "Error al realizar la búsqueda",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseCreateErrorGeneric.class)))
            }
    )
    public ResponseEntity<ApiResponseConsolidation<Object>> findById(
            @PathVariable("id") String id
    ){

        ResponseWrapper<User> user;

        //Validamos que el ID que nos proporcionan por la URL sea válido
        try {
            Long userId = Long.parseLong(id);
            user = userService.findById(userId);
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

        if( user.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            user.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Usuario obtenido por ID.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                user.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PutMapping("update-by-id/{id}")
    @Operation(
            summary = "Actualizar un usuario",
            description = "Actualizar un usuario dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID para la actualización", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario Actualizado Correctamente.",
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
    public ResponseEntity<ApiResponseConsolidation<Object>> update(
            @Valid
            @RequestBody UpdateUserDto userRequest,
            BindingResult result,
            @PathVariable("id") String id
    ){

        if(result.hasFieldErrors()){
            ErrorsValidationsResponse errors = new ErrorsValidationsResponse();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            errors.validation(result),
                            new ApiResponseConsolidation.Meta(
                                    "Errores en los campos de actualización",
                                    HttpStatus.BAD_REQUEST.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        ResponseWrapper<User> userUpdate;

        //Validamos que el ID de la URL sea válido
        try {
            Long userId = Long.parseLong(id);
            userUpdate = userService.update(userId, userRequest);
        }catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseConsolidation<>(
                            null,
                            new ApiResponseConsolidation.Meta(
                                    "El ID proporcionado es inválido.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        if( userUpdate.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            userUpdate.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Usuario Actualizado Correctamente.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                userUpdate.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

}
