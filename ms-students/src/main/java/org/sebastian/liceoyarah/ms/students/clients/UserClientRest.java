package org.sebastian.liceoyarah.ms.students.clients;


import org.sebastian.liceoyarah.ms.students.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Users;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "yarah-ms-users", url = "host.docker.internal:18882/business/v1/api/users")
public interface UserClientRest {

    @GetMapping("/find-by-document/{documentNumber}")
    ApiResponseConsolidation<Persons> getPerson(@PathVariable("documentNumber") String documentNumber);

    @GetMapping("/find-by-document/{documentNumber}")
    ApiResponseConsolidation<Users> getUser(@PathVariable("documentNumber") String documentNumber);

    //! Funcionalidad especial para la búsqueda por filtros
    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<String>> findPersonDocumentsByCriteria(@RequestParam("search") String search);

    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<String>> findUserDocumentsByCriteria(@RequestParam("search") String search);

}
