package org.sebastian.liceoyarah.ms.users.clients;

import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//Como vamos a hacer una comunicación entre contenedores locales, ya no sería localhost,
//usamos el host.docker.internal que nos ayuda como una especie de DNS para conectarnos,
//entonces, cambiamos el localhost por host.docker.internal
@FeignClient(name = "yarah-ms-persons", url = "host.docker.internal:18881/business/v1/api/persons")
public interface PersonClientRest {

    @GetMapping("/find-by-document/{documentNumber}")
    ApiResponseConsolidation<Persons> getPerson(@PathVariable("documentNumber") String documentNumber);

    //! Funcionalidad especial para la búsqueda por filtros
    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<String>> findPersonDocumentsByCriteria(@RequestParam("search") String search);

}
