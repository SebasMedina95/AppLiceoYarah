package org.sebastian.liceoyarah.ms.users.clients;

import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yarah-ms-persons", url = "localhost:18881/business/v1/api/persons")
public interface PersonClientRest {

    @GetMapping("/find-by-id/{id}")
    ApiResponseConsolidation<Persons> getPerson(@PathVariable("id") Long id);

    //! Funcionalidad especial para la búsqueda por filtros
    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<Long>> findPersonIdsByCriteria(@RequestParam("search") String search);

}
