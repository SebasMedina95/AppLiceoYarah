package org.sebastian.liceoyarah.ms.users.clients.requests;

import feign.FeignException;
import org.sebastian.liceoyarah.ms.users.clients.PersonClientRest;
import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetPersonMs {

    private static final Logger logger = LoggerFactory.getLogger(GetPersonMs.class);
    private final PersonClientRest personClientRest;

    @Autowired
    public GetPersonMs(
            PersonClientRest personClientRest
    ){
        this.personClientRest = personClientRest;
    }

    public Persons getPersonOfMsPersons(String documentPerson){

        try{

            //? Ahora hallemos la persona en su MS
            Persons personData;
            ApiResponseConsolidation<Persons> personMsvc =
                    personClientRest.getPerson(documentPerson);
            personData = personMsvc.getData();

            return personData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener la persona del MS Persons, error: ", fe);
            return null;
        }

    }

}
