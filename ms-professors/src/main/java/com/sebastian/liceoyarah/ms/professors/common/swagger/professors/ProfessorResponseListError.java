package com.sebastian.liceoyarah.ms.professors.common.swagger.professors;

import com.sebastian.liceoyarah.ms.professors.common.utils.ApiResponseConsolidation;
import com.sebastian.liceoyarah.ms.professors.common.utils.ErrorsValidationsResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta de error en la validación de paginación")
public class ProfessorResponseListError {

    @Schema(description = "Detalles de los errores de validación")
    private ErrorsValidationsResponse errors;

    @Schema(description = "Meta información sobre el error")
    private ApiResponseConsolidation.Meta meta;

}
