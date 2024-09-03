package org.sebastian.liceoyarah.ms.students.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sebastian.liceoyarah.ms.students.common.swagger.folios.FolioResponseCreate;
import org.sebastian.liceoyarah.ms.students.common.swagger.folios.FolioResponseCreateErrorFields;
import org.sebastian.liceoyarah.ms.students.common.swagger.folios.FolioResponseCreateErrorGeneric;
import org.sebastian.liceoyarah.ms.students.common.swagger.students.StudentResponseCreate;
import org.sebastian.liceoyarah.ms.students.common.swagger.students.StudentResponseCreateErrorGeneric;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.sebastian.liceoyarah.ms.students.common.utils.CustomPagedResourcesAssembler;
import org.sebastian.liceoyarah.ms.students.common.utils.ErrorsValidationsResponse;
import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.Student;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateFolioDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateStudentDto;
import org.sebastian.liceoyarah.ms.students.services.FolioService;
import org.sebastian.liceoyarah.ms.students.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/api/students")
@Tag(
        name = "Controlador de Estudiantes",
        description = "Operaciones relacionadas con el micro servicio de estudiantes - Operaciones de Estudiantes"
)
public class StudentController {

    private final StudentService studentService;
    private final CustomPagedResourcesAssembler<Student> customPagedResourcesAssembler;

    @Autowired
    public StudentController(
            StudentService studentService,
            CustomPagedResourcesAssembler<Student> customPagedResourcesAssembler
    ){
        this.studentService = studentService;
        this.customPagedResourcesAssembler = customPagedResourcesAssembler;
    }

    @PostMapping("/create")
    @Operation(summary = "Crear un Estudiante", description = "Creación de un estudiante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estudiante Registrado Correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolioResponseCreate.class))),
            @ApiResponse(responseCode = "406", description = "Errores en los campos de creación.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolioResponseCreateErrorFields.class))),
            @ApiResponse(responseCode = "400", description = "Cualquier otro caso de error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FolioResponseCreateErrorGeneric.class))),
    })
    public ResponseEntity<ApiResponseConsolidation<Object>> create(
            @Valid
            @RequestBody CreateStudentDto studentRequest,
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
        ResponseWrapper<Student> studentNew = studentService.create(studentRequest);

        //? Si no ocurre algún error, entonces registramos :)
        if( studentNew.getData() != null ){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseConsolidation<>(
                            studentNew.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Estudiante Registrado Correctamente.",
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
                                studentNew.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PostMapping("/find-all")
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(){

        return null;

    }

    @GetMapping("/find-by-id/{id}")
    @Operation(
            summary = "Obtener estudiante por ID",
            description = "Obtener un estudiante dado el ID",
            parameters = {
                    @Parameter(name = "id", description = "ID del estudiante a obtener", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponseCreate.class))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponseCreateErrorGeneric.class))),
                    @ApiResponse(responseCode = "400", description = "Error al realizar la búsqueda",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponseCreateErrorGeneric.class)))
            }
    )
    public ResponseEntity<ApiResponseConsolidation<Object>> findById(
            @PathVariable("id") String id
    ){

        ResponseWrapper<Student> student;

        //Validamos que el ID que nos proporcionan por la URL sea válido
        try {
            Long studentId = Long.parseLong(id);
            student = studentService.findById(studentId);
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

        if( student.getData() != null ){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseConsolidation<>(
                            student.getData(),
                            new ApiResponseConsolidation.Meta(
                                    "Estudiante obtenido por ID.",
                                    HttpStatus.OK.value(),
                                    LocalDateTime.now()
                            )
                    ));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseConsolidation<>(
                        null,
                        new ApiResponseConsolidation.Meta(
                                student.getErrorMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        )
                ));

    }

    @PutMapping("update-by-id/{id}")
    public ResponseEntity<ApiResponseConsolidation<Object>> update(){
        return null;
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ApiResponseConsolidation<Object>> delete(){
        return null;
    }


}
