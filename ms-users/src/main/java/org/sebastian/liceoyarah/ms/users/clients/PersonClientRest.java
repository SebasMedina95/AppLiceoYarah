package org.sebastian.liceoyarah.ms.users.clients;

import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//* Vamos a aplicar la comunicación entre los contenedores. Como estamos trabajando en local
//* tenemos varioas alternativas, trabajo directo en local con localhost, podemos usar el
//* host.docker.internal que es para poder conectarnos a la base de datos por fuera del hostname
//* a nivel del contenedor y está colocando el mismo nombre de la aplicación destino, como por ejemplo
//* yarah-ms-users, la última opción requiere que cada contenedor esté bajo la misma red dockerizada.

//Entonces, cambiamos el localhost por host.docker.internal para referir la red del equipo a nivel de Docker
//!@FeignClient(name = "yarah-ms-persons", url = "host.docker.internal:18881/business/v1/api/persons")

//Entonces, usamos localhost para referir la máquina local sin despliegue necesario de docker
//!@FeignClient(name = "yarah-ms-persons", url = "localhost:18881/business/v1/api/persons")

//Entonces, usamos el yarah-ms-persons ya que es el nombre del contenedor en la red networking interna - hostname
//? Para implementar esta tenemos que tener los contenedores operando en la misma red //?
@FeignClient(name = "yarah-ms-persons", url = "yarah-ms-persons:18881/business/v1/api/persons")
public interface PersonClientRest {

    @GetMapping("/find-by-document/{documentNumber}")
    ApiResponseConsolidation<Persons> getPerson(@PathVariable("documentNumber") String documentNumber);

    //! Funcionalidad especial para la búsqueda por filtros
    @GetMapping("/find-by-search")
    ApiResponseConsolidation<List<String>> findPersonDocumentsByCriteria(@RequestParam("search") String search);

}
