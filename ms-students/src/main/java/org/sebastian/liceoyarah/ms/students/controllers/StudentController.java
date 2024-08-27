package org.sebastian.liceoyarah.ms.students.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/students")
@Tag(name = "Controlador de Estudiantes", description = "Operaciones relacionadas con los estudiantes")
public class StudentController {

    @Autowired
    public StudentController(){}

    @PostMapping("/create")
    public ResponseEntity<ApiResponseConsolidation<Object>> create(){
        return null;
    }

    @PostMapping("/find-all")
    public ResponseEntity<ApiResponseConsolidation<Object>> findAll(){
        return null;
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponseConsolidation<Object>> findById(){
        return null;
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
