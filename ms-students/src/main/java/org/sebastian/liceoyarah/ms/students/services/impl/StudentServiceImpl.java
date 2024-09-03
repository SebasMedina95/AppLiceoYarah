package org.sebastian.liceoyarah.ms.students.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.students.clients.dtos.Users;
import org.sebastian.liceoyarah.ms.students.clients.requests.GetPersonsMs;
import org.sebastian.liceoyarah.ms.students.clients.requests.GetUserMs;
import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.Student;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateStudentDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.update.UpdateStudentDto;
import org.sebastian.liceoyarah.ms.students.repositories.FolioRepository;
import org.sebastian.liceoyarah.ms.students.repositories.StudentRepository;
import org.sebastian.liceoyarah.ms.students.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StudentServiceImpl implements StudentService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final GetUserMs getUserMs;
    private final GetPersonsMs getPersonsMs;
    private final StudentRepository studentRepository;
    private final FolioRepository folioRepository;

    @Autowired
    public StudentServiceImpl(
            GetUserMs getUserMs,
            GetPersonsMs getPersonsMs,
            StudentRepository studentRepository,
            FolioRepository folioRepository
    ){
        this.getUserMs = getUserMs;
        this.getPersonsMs = getPersonsMs;
        this.studentRepository = studentRepository;
        this.folioRepository = folioRepository;
    }

    @Override
    @Transactional
    public ResponseWrapper<Student> create(CreateStudentDto student) {

        logger.info("Iniciando Acción - MS Students - Creación de un estudiante");

        //? Validar no repetición de número de documento (Referencia MS Users)
        Optional<Student> documentNumberOptional = studentRepository.findByNumberDocument(student.getDocumentNumber());
        if( documentNumberOptional.isPresent() ){
            logger.error("No se puede crear el estudiante, número de documento ya asociado como estudiante: {}",
                    student.getDocumentNumber());
            return new ResponseWrapper<>(
                    null, "El estudiante con el documento " + student.getDocumentNumber() +
                    " ya se encuentra registrado a nivel de documento"
            );
        }

        //? Validamos que el Folio proporcionado sea permitido
        Optional<Folio> folioOptional = folioRepository.findById(student.getFolioId());
        if( folioOptional.isEmpty() ){
            logger.error("No se puede crear el estudiante, el folio no se ha encontrado con el ID: {}",
                    student.getFolioId());
            return new ResponseWrapper<>(
                    null, "El folio con el ID " + student.getFolioId() +
                    " no pudo ser hallado"
            );
        }

        Folio getFolio = folioOptional.orElseThrow();

        //? Procedemos a hacer extracción de data desde el MS por medio de Feign.
        Users userData;
        String userDocumentMs = student.getDocumentNumber();
        try{

            userData = getUserMs.getPersonOfMsPersons(userDocumentMs);
            if( userData == null ){
                logger.warn("Ocurrió algo en el servicio de MS Users, persona no hallada o MS caído");
                return new ResponseWrapper<>(
                        null,
                        "El usuario para ser asociado al estudiante no fue hallada en la búsqueda"
                );
            }

        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener el usuario del MS Users, error: ", fe);
            return new ResponseWrapper<>(
                    null, "El usuario para ser asociada al estudiante no fue hallado"
            );
        }

        //? Debemos de generar el código ISBTTS (Fecha Nacimiento + Número Documento)
        String dateStr = String.valueOf(userData.getPerson().getBirthDate());
        LocalDate dateConv = LocalDate.parse(dateStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateInTostring = dateConv.format(formatter);
        String codeIsbtts = dateInTostring + userData.getDocumentNumber();

        //? Debemos ajustar el/los responsables
        List<String> personsCharge = student.getPersonsCharge();
        Map<String, String> personMap = new HashMap<>();
        for (String person : personsCharge) {

            try {

                Persons getPersonForPersonCharge = getPersonsMs.getPersonOfMsPersons(person);
                String document = getPersonForPersonCharge.getDocumentNumber();

                //Un acudiente no puede ser estudiante
                Optional<Student> getStudentOptional = studentRepository.findByNumberDocument(document);
                if (getStudentOptional.isEmpty()) {
                    String name =
                            getPersonForPersonCharge.getFirstName() + " " +
                                    getPersonForPersonCharge.getSecondName() + " " +
                                    getPersonForPersonCharge.getFirstSurname() + " " +
                                    getPersonForPersonCharge.getSecondSurname();
                    personMap.put(document, name);
                }

            } catch (Exception e) {
                logger.error("Error en MS Person o no se halló la persona con documento {}", person);
            }
        }

        String personChangeInString;
        if( personMap.size() != personsCharge.size() ){
            logger.error("Alguno de los acudientes a registrar no se encuentra en el MS Persons");
            return new ResponseWrapper<>(
                    null, "Alguno de los acudientes a registrar no se encuentra en el MS Persons " +
                    "o se encuentra registrado como estudiante"
            );
        }else{

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                personChangeInString = objectMapper.writeValueAsString(personMap);
            } catch (JsonProcessingException e) {
                logger.error("Error al convertir los acudiantes en un String válido");
                return new ResponseWrapper<>(
                        null, "Error al convertir los acudiantes en un String válido"
                );
            }

        }

        //? Llegamos hasta acá, ahora si guardamos Guardar usuario
        Student newStudent = new Student();
        newStudent.setFolio(getFolio);
        newStudent.setDocumentNumber(student.getDocumentNumber().trim());
        newStudent.setIsbttsCode(codeIsbtts);
        newStudent.setYearInscription(student.getYearInscription());
        newStudent.setSpecialCondition(student.getSpecialCondition());
        newStudent.setPersonsCharge(personChangeInString);
        newStudent.setDescription(student.getDescription());
        newStudent.setStatus(true); //* Por defecto entra en true
        newStudent.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
        newStudent.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
        newStudent.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
        newStudent.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

        logger.info("Estudiante creado correctamente");
        return new ResponseWrapper<>(studentRepository.save(newStudent), "Estudiante guardado correctamente");

    }

    @Override
    public Page<Student> findAll(String search, Pageable pageable) {

        return null;

    }

    @Override
    public ResponseWrapper<Student> findById(Long id) {

        logger.info("Iniciando Acción - Obtener un estudiante dado su ID - MS Students");

        try{

            Optional<Student> studentOptional = studentRepository.findById(id);

            if( studentOptional.isPresent() ){
                Student student = studentOptional.orElseThrow();
                logger.info("Estudiante obtenido por su ID");

                String userDocumentMs = student.getDocumentNumber();
                Users userData = getUserMs.getPersonOfMsPersons(userDocumentMs);
                student.setPerson(userData);

                return new ResponseWrapper<>(student, "Estudiante encontrado por ID correctamente");

            }

            logger.info("El estudiante no pudo ser encontrado cone el ID {}", id);
            return new ResponseWrapper<>(null, "El estudiante no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener estudiante por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El estudiante no pudo ser encontrado por el ID");

        }

    }

    @Override
    public ResponseWrapper<Student> update(Long id, UpdateStudentDto student) {
        return null;
    }

    @Override
    public ResponseWrapper<Student> delete(Long id) {
        return null;
    }
}
