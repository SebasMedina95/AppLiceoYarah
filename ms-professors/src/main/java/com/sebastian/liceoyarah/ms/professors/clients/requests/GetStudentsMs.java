package com.sebastian.liceoyarah.ms.professors.clients.requests;

import com.sebastian.liceoyarah.ms.professors.clients.StudentClientRest;
import com.sebastian.liceoyarah.ms.professors.clients.UserClientRest;
import com.sebastian.liceoyarah.ms.professors.clients.dtos.Students;
import com.sebastian.liceoyarah.ms.professors.clients.dtos.Users;
import com.sebastian.liceoyarah.ms.professors.common.utils.ApiResponseConsolidation;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
public class GetStudentsMs {

    private static final Logger logger = LoggerFactory.getLogger(GetStudentsMs.class);
    private final StudentClientRest studentClientRest;

    @Autowired
    public GetStudentsMs(
            StudentClientRest studentClientRest
    ){
        this.studentClientRest = studentClientRest;
    }

    public Students getStudentOfMsStudents(String documentStudent){

        try{

            //? Ahora hallemos el usuario en su MS
            Students studentsData;
            ApiResponseConsolidation<Students> studentMsvc =
                    studentClientRest.getStudent(documentStudent);
            studentsData = studentMsvc.getData();

            return studentsData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener el estudiante del MS Students, error: ", fe);
            return null;
        }

    }

    public List<String> getListStudentOfMsStudentByCriterial(String documentStudent){

        try{

            //? Ahora hallemos la persona en su MS
            List<String> studentData;
            ApiResponseConsolidation<List<String>> userMsvc =
                    studentClientRest.findStudentsDocumentsByCriteria(documentStudent);
            studentData = userMsvc.getData();

            return studentData;

        }catch (FeignException fe){
            logger.error("No pudimos obtener el estudiante del MS Students, error: ", fe);
            return Collections.emptyList();
        }

    }

}
