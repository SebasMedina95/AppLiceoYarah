package com.sebastian.liceoyarah.ms.professors.clients.requests;

import feign.FeignException;
import org.sebastian.liceoyarah.ms.students.clients.UserClientRest;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Users;
import org.sebastian.liceoyarah.ms.students.common.utils.ApiResponseConsolidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class GetUserMs {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMs.class);
    private final UserClientRest userClientRest;

    @Autowired
    public GetUserMs(
            UserClientRest userClientRest
    ){
        this.userClientRest = userClientRest;
    }

    public Users getPersonOfMsPersons(String documentPerson){

        try{

            //? Ahora hallemos el usuario en su MS
            Users usersData;
            ApiResponseConsolidation<Users> userMsvc =
                    userClientRest.getUser(documentPerson);
            usersData = userMsvc.getData();

            return usersData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener el usuario del MS Users, error: ", fe);
            return null;
        }

    }

    public List<String> getListUserOfMsUsersByCriterial(String documentPerson){

        try{

            //? Ahora hallemos la persona en su MS
            List<String> userData;
            ApiResponseConsolidation<List<String>> userMsvc =
                    userClientRest.findUserDocumentsByCriteria(documentPerson);
            userData = userMsvc.getData();

            return userData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener el usuario del MS Users, error: ", fe);
            return Collections.emptyList();
        }

    }

}
