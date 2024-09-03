package org.sebastian.liceoyarah.ms.students.clients.requests;

import feign.FeignException;
import org.sebastian.liceoyarah.ms.students.clients.PersonClientRest;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetPersonsMs {

    private static final Logger logger = LoggerFactory.getLogger(GetPersonsMs.class);
    private final PersonClientRest personClientRest;

    @Autowired
    public GetPersonsMs(
            PersonClientRest personClientRest
    ){
        this.personClientRest = personClientRest;
    }

    public Persons getPersonOfMsPersons(String documentPerson){

        try{

            //? Ahora hallemos la persona en su MS
            Persons personsData;
            ApiResponseConsolidation<Persons> personMsvc =
                    personClientRest.getPerson(documentPerson);
            personsData = personMsvc.getData();

            return personsData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener la persona del MS Persons, error: ", fe);
            return null;
        }

    }

}
