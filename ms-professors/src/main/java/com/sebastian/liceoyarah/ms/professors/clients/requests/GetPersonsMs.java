package com.sebastian.liceoyarah.ms.professors.clients.requests;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;
import java.util.List;

import com.sebastian.liceoyarah.ms.professors.clients.PersonClientRest;
import com.sebastian.liceoyarah.ms.professors.clients.dtos.Persons;
import com.sebastian.liceoyarah.ms.professors.common.utils.ApiResponseConsolidation;

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

    public List<String> getListPersonOfMsPersonsByCriterial(String documentPerson){

        try{

            //? Ahora hallemos la persona en su MS
            List<String> personData;
            ApiResponseConsolidation<List<String>> personMsvc =
                    personClientRest.findPersonDocumentsByCriteria(documentPerson);
            personData = personMsvc.getData();

            return personData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener la persona del MS Persons, error: ", fe);
            return Collections.emptyList();
        }

    }

}
