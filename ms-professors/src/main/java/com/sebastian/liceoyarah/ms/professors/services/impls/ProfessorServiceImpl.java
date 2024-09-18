package com.sebastian.liceoyarah.ms.professors.services.impls;

import com.sebastian.liceoyarah.ms.professors.clients.StudentClientRest;
import com.sebastian.liceoyarah.ms.professors.clients.dtos.Students;
import com.sebastian.liceoyarah.ms.professors.clients.dtos.Users;
import com.sebastian.liceoyarah.ms.professors.clients.requests.GetPersonsMs;
import com.sebastian.liceoyarah.ms.professors.clients.requests.GetUserMs;
import com.sebastian.liceoyarah.ms.professors.common.utils.ResponseWrapper;
import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.create.CreateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.update.UpdateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.repositories.ProfessorRepository;
import com.sebastian.liceoyarah.ms.professors.services.ProfessorService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(ProfessorServiceImpl.class);

    private final GetUserMs getUserMs;
    private final StudentClientRest getStudentsMs;
    private final ProfessorRepository professorRepository;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public ProfessorServiceImpl(
            GetUserMs getUserMs,
            StudentClientRest getStudentsMs,
            ProfessorRepository professorRepository
    ){
        this.getUserMs = getUserMs;
        this.getStudentsMs = getStudentsMs;
        this.professorRepository = professorRepository;
    }

    @Override
    public ResponseWrapper<Professor> create(CreateProfessorDto professor) {

        logger.info("Iniciando Acción - MS Professors - Creación de un profesor");

        //? Validamos primero que no exista registrado ya como profesor el documento.
        Optional<Professor> documentNumberOptional = professorRepository.findByNumberDocument(professor.getDocumentNumber());
        if( documentNumberOptional.isPresent() ){
            logger.error("No se puede crear el profesor, número de documento ya asociado como profesor: {}",
                    professor.getDocumentNumber());
            return new ResponseWrapper<>(
                    null, "El profesor con el documento " + professor.getDocumentNumber() +
                    " ya se encuentra registrado a nivel de documento"
            );
        }

        //? Validamos primero que no exista la tarjeta profesional registrada.
        Optional<Professor> cardTittleOptional = professorRepository.findByCardTittle(professor.getCardTittle());
        if( cardTittleOptional.isPresent() ){
            logger.error("No se puede crear el profesor, Tarjeta Profesional ya existente: {}",
                    professor.getCardTittle());
            return new ResponseWrapper<>(
                    null, "El profesor con la tarjeta profesional " + professor.getCardTittle() +
                    " ya se encuentra registrado a nivel de su tarjeta"
            );
        }

        //? Validamos que no exista como estudiante
        // Un estudiante para este caso no puede ser profesor, es un tema importante.
        Students studentData;
        String studentDocumentMs = professor.getDocumentNumber();
        try{

            studentData = getStudentsMs.getStudent(studentDocumentMs).getData();
            if( studentData != null ){
                logger.warn("Fue encontrado estudiante con documento, no podemos proceder crearlo como profesor: {}", professor.getDocumentNumber());
                return new ResponseWrapper<>(
                        null,
                        "No podemos ingresar un estudiante como profesor, documento: " + professor.getDocumentNumber()
                );
            }

        }catch (FeignException fe){

            // Obtener el código de estado HTTP de la excepción
            int statusCode = fe.status();

            // Si el error es 404 (no encontrado), manejamos específicamente este caso
            //? Si cae acá entonces podemos seguir porque no está como estudiante
            if (statusCode == 404 || statusCode == 400) {

                logger.info("El estudiante no fue encontrado por el número de documento, VÁLIDO PARA PROFESOR, podemos continuar con documento: {}",
                        professor.getDocumentNumber());

            }else{
                // Si es cualquier otro código, lo tratamos como un error del microservicio o del sistema
                logger.error("Ocurrió un error al intentar obtener el estudiante del MS Students, error: ", fe);
                return new ResponseWrapper<>(
                        null, "Validación de estudiante respecto a profesor fallida"
                );
            }

        }

        //? Validamos que exista como usuario
        Users userData;
        String userDocumentMs = professor.getDocumentNumber();
        try{

            userData = getUserMs.getPersonOfMsPersons(userDocumentMs);
            if( userData == null ){
                logger.warn("Ocurrió algo en el servicio de MS Users, persona no hallada o MS caído");
                return new ResponseWrapper<>(
                        null,
                        "El usuario para ser asociado al profesor no fue hallado en la búsqueda"
                );
            }

        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener el usuario del MS Users, error: ", fe);
            return new ResponseWrapper<>(
                    null, "El usuario para ser asociada al profesor no fue hallado"
            );
        }

        //? Creamos el correo institucional
        String domain = "@liceoyarah.edu.co";
        String firstName = userData.getPerson().getFirstName();
        String firstSurname = userData.getPerson().getFirstSurname();
        String randomNumber = this.generateRandomNumber();
        String institutionalEmailConcat = (firstName + "." + firstSurname + "." + randomNumber + domain).toLowerCase();

        Professor newProfessor = new Professor();
        newProfessor.setDocumentNumber(professor.getDocumentNumber());
        newProfessor.setType(professor.getType());
        newProfessor.setProffesionalTittle(professor.getProffesionalTittle());
        newProfessor.setCardTittle(professor.getCardTittle());
        newProfessor.setGroupDirector(professor.getGroupDirector());
        newProfessor.setTechnicalProfessor(professor.getTechnicalProfessor());
        newProfessor.setCore(professor.getCore());
        newProfessor.setLaborDay(professor.getLaborDay());
        newProfessor.setDescription(professor.getDescription());
        newProfessor.setInstitutionalEmail(institutionalEmailConcat);
        newProfessor.setStatus(true); //* Por defecto entra en true
        newProfessor.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
        newProfessor.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
        newProfessor.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
        newProfessor.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

        logger.info("Profesor creado correctamente");
        return new ResponseWrapper<>(professorRepository.save(newProfessor), "Profesor guardado correctamente");

    }

    @Override
    public Page<Professor> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<Professor> findById(Long id) {

        logger.info("Iniciando Acción - Obtener un profesor dado su ID - MS Professor");

        try{

            Optional<Professor> professorOptional = professorRepository.findById(id);

            if( professorOptional.isPresent() ){
                Professor professor = professorOptional.orElseThrow();
                logger.info("Profesor obtenido por su ID");

                String userDocumentMs = professor.getDocumentNumber();
                Users userData = getUserMs.getPersonOfMsPersons(userDocumentMs);
                professor.setUser(userData);

                return new ResponseWrapper<>(professor, "Profesor encontrado por ID correctamente");

            }

            logger.info("El profesor no pudo ser encontrado cone el ID {}", id);
            return new ResponseWrapper<>(null, "El profesor no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener profesor por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El profesor no pudo ser encontrado por el ID");

        }

    }

    @Override
    public ResponseWrapper<Professor> update(Long id, UpdateProfessorDto professor) {
        return null;
    }

    @Override
    public ResponseWrapper<Professor> delete(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<Professor> findByDocument(Long documentNumber) {
        return null;
    }

    public String generateRandomNumber() {
        StringBuilder randomNumber = new StringBuilder();

        // Generar 4 números aleatorios entre 0 y 9
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10); // nextInt(10) genera un número entre 0 y 9
            randomNumber.append(digit);
        }

        return randomNumber.toString();
    }

}
