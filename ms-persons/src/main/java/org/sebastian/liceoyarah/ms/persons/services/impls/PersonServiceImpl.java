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
import org.springframework.data.jpa.domain.Specification;
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
    public ResponseWrapper<Person> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<Person> update(Long id, UpdatePersonDto person) {
        return null;
    }

    @Override
    public ResponseWrapper<Person> delete(Long id) {
        return null;
    }

    @Override
    public List<Long> findPersonIdsByCriteria(String search) {
        return List.of();
    }
}
