package com.sebastian.liceoyarah.ms.professors.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/professors")
@Tag(
        name = "Controlador de Profesores",
        description = "Operaciones relacionadas con el micro servicio de profesores - Operaciones de Profesores"
)
public class ProfessorController {
}
