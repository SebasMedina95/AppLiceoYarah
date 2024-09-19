package com.sebastian.liceoyarah.ms.professors.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(ProfessorServiceImpl.class);

    private final GetUserMs getUserMs;
    private final StudentClientRest getStudentsMs;
    private final GetPersonsMs getPersonsMs;
    private final ProfessorRepository professorRepository;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public ProfessorServiceImpl(
            GetUserMs getUserMs,
            StudentClientRest getStudentsMs,
            GetPersonsMs getPersonsMs,
            ProfessorRepository professorRepository
    ){
        this.getUserMs = getUserMs;
        this.getStudentsMs = getStudentsMs;
        this.getPersonsMs = getPersonsMs;
        this.professorRepository = professorRepository;
    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public Page<Professor> findAll(String search, Pageable pageable) {

        logger.info("Obtener todos los profesores paginados y con filtro - MS Professor");

        // Si tenemos criterio de búsqueda entonces hacemos validaciones
        Page<Professor> professorPage;
        List<String> personDocuments;

        if (search != null && !search.isEmpty()) {

            // Buscamos primero en el MS de personas para traer la información que coincida con el criterio
            logger.info("Obtener todos los usuarios - Con criterio de búsqueda en MS Persons y Users");
            personDocuments = getPersonsMs.getListPersonOfMsPersonsByCriterial(search);

            // Ahora realizamos la búsqueda en este MS tanto con los ID hallados desde MS de Personas como
            // con la posibilidad de que también haya un criterio adicional de coincidencia acá.
            logger.info("Obtener todos los profesores - Aplicando la paginación luego de filtro");
            professorPage = professorRepository.findFilteredProfessor(search, personDocuments, pageable);

        }else{

            // Si llegamos a este punto paginamos normal sin el buscador.
            logger.info("Obtener todos los profesores - Sin criterio de búsqueda");
            professorPage = professorRepository.findNoFilteredProfessor(pageable);

        }

        // Ajustamos los elementos hallados en el content para que aparezcan junto con la información
        // que viene desde el MS de personas.
        logger.info("Aplicamos contra llamado a MS Persons para adecuar response a Frontend");
        List<Professor> userDtos = professorPage.getContent().stream()
                .map(u -> {
                    String personDocument = u.getDocumentNumber();
                    Users user = getUserMs.getPersonOfMsPersons(personDocument);
                    u.setUser(user);
                    return u;
                })
                .toList();

        // Retornamos los elementos con la paginación y filtro aplicado.
        logger.info("Listado de personas obtenido con toda la data requerida");
        return new PageImpl<>(userDtos, pageable, professorPage.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional
    public ResponseWrapper<Professor> update(Long id, UpdateProfessorDto professor) {

        logger.info("Iniciando Acción - Actualizar un profesor dado su ID - MS Professor");
        Optional<Professor> professorOptional = professorRepository.findById(id);

        if( professorOptional.isPresent() ){

            Professor professorDb = professorOptional.orElseThrow();

            //? Validamos que no se repita la tarjeta profesional
            Optional<Professor> cardTittleOptional =
                    professorRepository.getProfessorByCardTittleForEdit(professor.getCardTittle(), id);
            if( cardTittleOptional.isPresent() ){
                logger.error("No se puede actualizar el profesor, Tarjeta Profesional ya existente: {}",
                        professor.getCardTittle());
                return new ResponseWrapper<>(
                        null, "El profesor con la tarjeta profesional " + professor.getCardTittle() +
                        " ya se encuentra registrado a nivel de su tarjeta"
                );
            }

            //? Vamos a actualizar si llegamos hasta acá
            professorDb.setType(professor.getType());
            professorDb.setProffesionalTittle(professor.getProffesionalTittle());
            professorDb.setCardTittle(professor.getCardTittle());
            professorDb.setGroupDirector(professor.getGroupDirector());
            professorDb.setTechnicalProfessor(professor.getTechnicalProfessor());
            professorDb.setCore(professor.getCore());
            professorDb.setLaborDay(professor.getLaborDay());
            professorDb.setDescription(professor.getDescription());
            professorDb.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
            professorDb.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

            logger.info("El profesor fue actualizado correctamente");
            return new ResponseWrapper<>(professorRepository.save(professorDb), "Profesor Actualizado Correctamente");

        }else{

            logger.warn("El profesor por el ID no fue encontrado");
            return new ResponseWrapper<>(null, "El profesor no fue encontrado");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<Professor> delete(Long id) {

        logger.info("Iniciando Acción - Eliminar un profesor dado su ID - MS Professor");

        try{

            Optional<Professor> professorOptional = professorRepository.findById(id);

            if( professorOptional.isPresent() ){

                Professor professorDb = professorOptional.orElseThrow();

                //? Vamos a actualizar si llegamos hasta acá
                //? ESTO SERÁ UN ELIMINADO LÓGICO!
                professorDb.setStatus(false);
                professorDb.setUserUpdated("usuario123");
                professorDb.setDateUpdated(new Date());

                return new ResponseWrapper<>(professorRepository.save(professorDb), "Profesor Eliminado Correctamente");

            }else{

                return new ResponseWrapper<>(null, "El profesor no fue encontrado");

            }

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar eliminar lógicamente profesor por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El profesor no pudo ser eliminado");

        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<Professor> findByDocument(Long documentNumber) {

        logger.info("Iniciando Acción - Obtener un profesor dado su Número de Documento - MS Professors");

        try{

            Optional<Professor> professorOptional = professorRepository.findByNumberDocument(documentNumber.toString());

            if( professorOptional.isPresent() ){
                Professor professor = professorOptional.orElseThrow();
                logger.info("Profesor obtenido por su Número de Documento");

                String userDocumentMs = professor.getDocumentNumber();
                Users userData = getUserMs.getPersonOfMsPersons(userDocumentMs);
                professor.setUser(userData);

                return new ResponseWrapper<>(professor, "Profesor encontrado por Número de Documento correctamente");

            }

            logger.info("El profesor no pudo ser encontrado con el Número de Documento {}", documentNumber);
            return new ResponseWrapper<>(null, "El profesor no pudo ser encontrado por el Número de Documento " + documentNumber);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener profesor por Número de Documento, detalles ...", err);
            return new ResponseWrapper<>(null, "El profesor no pudo ser encontrado por el Número de Documento");

        }

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
