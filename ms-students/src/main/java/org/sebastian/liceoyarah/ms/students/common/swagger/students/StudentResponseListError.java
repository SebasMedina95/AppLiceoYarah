package org.sebastian.liceoyarah.ms.students.common.swagger.students;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.sebastian.liceoyarah.ms.students.common.utils.ErrorsValidationsResponse;


@Data
@Schema(description = "Respuesta de error en la validación de paginación")
public class StudentResponseListError {

    @Schema(description = "Detalles de los errores de validación")
    private ErrorsValidationsResponse errors;

    @Schema(description = "Meta información sobre el error")
    private ApiResponseConsolidation.Meta meta;

}
