package org.sebastian.liceoyarah.ms.persons.services.impls;

import org.sebastian.liceoyarah.ms.persons.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.persons.entities.Person;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.create.CreatePersonDto;
import org.sebastian.liceoyarah.ms.persons.entities.dtos.update.UpdatePersonDto;
import org.sebastian.liceoyarah.ms.persons.repositories.PersonRepository;
import org.sebastian.liceoyarah.ms.persons.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;

    @Autowired
    public PersonServiceImpl(
            PersonRepository personRepository
    ){
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public ResponseWrapper<Person> create(CreatePersonDto person) {

        logger.info("Iniciando Acción - MS Persons - Creación de una persona");

        try {

            //? Validemos que no se repita la persona
            String personDocument = person.getDocumentNumber().trim().toUpperCase();
            String personEmail = person.getEmail().trim().toUpperCase();
            Optional<Person> getPersonOptional = personRepository.getPersonByDocumentAndByEmail(personEmail, personDocument);
            if( getPersonOptional.isPresent() )
                return new ResponseWrapper<>(null, "El documento de la persona o su email ya está registrado");

            //? Pasamos hasta acá, registramos persona
            Person newPerson = new Person();
            newPerson.setDocumentType(person.getDocumentType());
            newPerson.setDocumentNumber(person.getDocumentNumber());
            newPerson.setFirstName(person.getFirstName());
            newPerson.setSecondName(person.getSecondName());
            newPerson.setFirstSurname(person.getFirstSurname());
            newPerson.setSecondSurname(person.getSecondSurname());
            newPerson.setEmail(person.getEmail());
            newPerson.setGender(person.getGender());
            newPerson.setPhone1(person.getPhone1());
            newPerson.setPhone2(person.getPhone2());
            newPerson.setAddress(person.getAddress());
            newPerson.setNeighborhood(person.getNeighborhood());
            newPerson.setDescription(person.getDescription());
            newPerson.setCivilStatus(person.getCivilStatus());
            newPerson.setBirthDate(person.getBirthDate());
            newPerson.setStatus(true); //* Por defecto entra en true
            newPerson.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
            newPerson.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
            newPerson.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
            newPerson.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

            logger.info("Persona creada correctamente");
            return new ResponseWrapper<>(personRepository.save(newPerson), "Persona guardada correctamente");

        }catch (Exception ex){

            logger.error("Ocurrió un error al intentar crear la persona, detalles ...", ex);
            return new ResponseWrapper<>(null, "La persona no pudo ser creada");

        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Person> findAll(String search, Pageable pageable) {

        logger.info("Iniciando Acción - MS Persons - Obtener todas las personas paginadas y con filtro");
        Page<Person> persons = personRepository.findGeneralPersonsByCriteria(search, pageable);

        logger.info("Listado de personas obtenida");
        return persons;

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<Person> findById(Long id) {

        logger.info("Iniciando Acción - MS Persons - Obtener una persona dado su ID");

        try{

            Optional<Person> personOptional = personRepository.findById(id);

            if( personOptional.isPresent() ){
                Person person = personOptional.orElseThrow();
                logger.info("Persona obtenida por su ID");
                return new ResponseWrapper<>(person, "Persona encontrada por ID correctamente");
            }

            logger.info("La persona no pudo ser encontrada cone el ID {}", id);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el ID");

        }

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<Person> findByNumberDocument(String numberDocument) {

        logger.info("Iniciando Acción - MS Persons - Obtener una persona dado su Número de Documento");

        try{

            Optional<Person> personOptional = personRepository.findByNumberDocument(numberDocument);

            if( personOptional.isPresent() ){
                Person person = personOptional.orElseThrow();
                logger.info("Persona obtenida por su Número de Documento");
                return new ResponseWrapper<>(person, "Persona encontrada por Número de Documento correctamente");
            }

            logger.info("La persona no pudo ser encontrada con el Número de Documento {}", numberDocument);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el Número de Documento " + numberDocument);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener persona por Número de Documento, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser encontrado por el Número de Documento");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<Person> update(Long id, UpdatePersonDto person) {

        logger.info("Iniciando Acción - MS Persons - Actualizar una persona dado su ID");

        try{

            Optional<Person> personOptional = personRepository.findById(id);
            if( personOptional.isPresent() ){

                Person personDb = personOptional.orElseThrow();

                //? Validemos que no se repita la persona
                String personEmail = person.getEmail().trim().toUpperCase();
                Optional<Person> getPersonOptionalByDocumentAndEmail = personRepository.getPersonByDocumentAndEmailForEdit(personEmail, id);

                if( getPersonOptionalByDocumentAndEmail.isPresent() ){
                    logger.info("La persona no se puede actualizar porque el email ya está registrado");
                    return new ResponseWrapper<>(null, "El email de la persona ya está registrado");
                }

                //? Vamos a actualizar si llegamos hasta acá
                personDb.setFirstName(person.getFirstName());
                personDb.setSecondName(person.getSecondName());
                personDb.setFirstSurname(person.getFirstSurname());
                personDb.setSecondSurname(person.getSecondSurname());
                personDb.setEmail(person.getEmail());
                personDb.setGender(person.getGender());
                personDb.setPhone1(person.getPhone1());
                personDb.setPhone2(person.getPhone2());
                personDb.setAddress(person.getAddress());
                personDb.setNeighborhood(person.getNeighborhood());
                personDb.setDescription(person.getDescription());
                personDb.setCivilStatus(person.getCivilStatus());
                personDb.setBirthDate(person.getBirthDate());
                personDb.setStatus(true); //* Por defecto entra en true
                personDb.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
                personDb.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

                logger.info("La persona fue actualizada correctamente");
                return new ResponseWrapper<>(personRepository.save(personDb), "Persona Actualizada Correctamente");

            }else{

                return new ResponseWrapper<>(null, "La persona no fue encontrada");

            }

        }catch (Exception err){

            logger.error("Ocurrió un error al intentar actualizar persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser actualizada");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<Person> delete(Long id) {

        try{

            Optional<Person> personOptional = personRepository.findById(id);

            if( personOptional.isPresent() ){

                Person personDb = personOptional.orElseThrow();

                //? Vamos a actualizar si llegamos hasta acá
                //? ESTO SERÁ UN ELIMINADO LÓGICO!
                personDb.setStatus(false);
                personDb.setUserUpdated("usuario123");
                personDb.setDateUpdated(new Date());

                return new ResponseWrapper<>(personRepository.save(personDb), "Persona Eliminada Correctamente");

            }else{

                return new ResponseWrapper<>(null, "La persona no fue encontrado");

            }

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar eliminar lógicamente persona por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "La persona no pudo ser eliminada");

        }

    }

    // Esta funcionalidad tiene el propósito de realizar búsquedas anidadas desde otros micros
    // por ejemplo, desde el micro de usuarios y estudiantes poder buscar a nivel también de la
    // persona potencialmente vinculada. Para este ejercicio, harémos que se devuelva un listado
    // a nivel de documentos,
    // ? EL DOCUMENTO SERÁ EL CAMPO REFERENCIA ASOCIABLE EN TODA LA APLICACIÓN ?//
    @Override
    @Transactional(readOnly = true)
    public List<Long> findPersonIdsByCriteria(String search) {

        return personRepository.findIdsByCriteria(search);

    }
}
