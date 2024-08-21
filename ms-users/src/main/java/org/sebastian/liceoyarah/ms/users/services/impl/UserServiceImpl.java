package org.sebastian.liceoyarah.ms.users.services.impl;

import feign.FeignException;
import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.clients.requests.GetPersonMs;
import org.sebastian.liceoyarah.ms.users.common.utils.ApiResponseConsolidation;
import org.sebastian.liceoyarah.ms.users.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.users.entities.User;
import org.sebastian.liceoyarah.ms.users.entities.dtos.create.CreateUserDto;
import org.sebastian.liceoyarah.ms.users.entities.dtos.update.UpdateUserDto;
import org.sebastian.liceoyarah.ms.users.repositories.UserRepository;
import org.sebastian.liceoyarah.ms.users.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    static String dummiesUser = "usuario123";
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final GetPersonMs getPersonMs;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            GetPersonMs getPersonMs,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.getPersonMs = getPersonMs;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseWrapper<User> create(CreateUserDto user) {

        logger.info("Iniciando guardado de usuario - MS User");

        //? Validar no repetición de email
        Optional<User> emailOptional = userRepository.findByEmail(user.getEmail());
        if( emailOptional.isPresent() ){
            logger.error("No se puede crear el usuario, email ya existe: {}", user.getEmail());
            return new ResponseWrapper<>(
                    null, "El email " + user.getEmail() + " ya se encuentra registrado"
            );
        }

        //? Validar no repetición de número de documento (Referencia MS Persons)
        Optional<User> documentNumberOptional = userRepository.findByNumberDocument(user.getDocumentNumber());
        if( documentNumberOptional.isPresent() ){
            logger.error("No se puede crear el usuario, número de documento ya asociado como usuario: {}", user.getDocumentNumber());
            return new ResponseWrapper<>(
                    null, "El usuario con el documento " + user.getDocumentNumber() + " ya se encuentra registrado"
            );
        }

        //? Procedemos a hacer extracción de data desde el MS por medio de Feign.
        Persons personData;
        String personDocumentMs = user.getDocumentNumber();
        try{

            personData = getPersonMs.getPersonOfMsPersons(personDocumentMs);
            if( personData == null ){
                logger.warn("Ocurrió algo en el servicio de MS Persons, persona no hallada o MS caído");
                return new ResponseWrapper<>(
                        null,
                        "La persona para ser asociada al usuario no fue hallada en la búsqueda"
                );
            }

        }catch (FeignException fe){
            logger.error("Ocurrió un error al intentar obtener la persona del MS Persons, error: ", fe);
            return new ResponseWrapper<>(
                    null, "La persona para ser asociada al usuario no fue hallada"
            );
        }

        //? Generar username
        //Tomamos el documento + primer nombre
        String username = personData.getDocumentNumber().trim() + personData.getFirstName().trim();

        //* Validar que no se repita username
        Optional<User> userOptional = userRepository.findByUserName(username);
        if( userOptional.isPresent() ){
            logger.error("No se puede crear el usuario, username ya existe: {}", username);
            return new ResponseWrapper<>(
                    null, "El usuario con el username " + username + " ya se encuentra registrado"
            );
        }

        //? Generar contraseña
        // Por facilidad de pruebas el mismo username
        String encodedPassword = passwordEncoder.encode(username);

        //? Guardar usuario
        User newUser = new User();
        newUser.setDocumentNumber(user.getDocumentNumber().trim());
        newUser.setEmail(user.getEmail().toLowerCase().trim());
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword);
        newUser.setStatus(true); //* Por defecto entra en true
        newUser.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
        newUser.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
        newUser.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
        newUser.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

        //? Enviar email de credenciales
        //TODO ...

        //? Guardamos y devolvemos al usuario
        logger.info("Usuario guardado correctamente");
        return new ResponseWrapper<>(
                userRepository.save(newUser),
                "El usuario fue creado correctamente"
        );

    }

    @Override
    public Page<User> findAll(String search, Pageable pageable) {

        logger.info("Obtener todos los usuarios paginados y con filtro - MS User");

        // Si tenemos criterio de búsqueda entonces hacemos validaciones
        Page<User> userPage;
        List<String> personDocuments;
        if (search != null && !search.isEmpty()) {

            // Buscamos primero en el MS de personas para traer la información que coincida con el criterio
            logger.info("Obtener todos los usuarios - Con criterio de búsqueda en MS Persons y Users");
            personDocuments = getPersonMs.getListPersonOfMsPersonsByCriterial(search);

            // Ahora realizamos la búsqueda en este MS tanto con los ID hallados desde MS de Personas como
            // con la posibilidad de que también haya un criterio adicional de coincidencia acá.
            logger.info("Obtener todos los deportistas - Aplicando la paginación luego de filtro");
            userPage = userRepository.findFilteredUser(search, personDocuments, pageable);

        }else{

            // Si llegamos a este punto paginamos normal sin el buscador.
            logger.info("Obtener todos los deportistas - Sin criterio de búsqueda");
            userPage = userRepository.findNoFilteredUser(pageable);

        }

        // Ajustamos los elementos hallados en el content para que aparezcan junto con la información
        // que viene desde el MS de personas.
        logger.info("Aplicamos contra llamado a MS Persons para adecuar response a Frontend");
        List<User> userDtos = userPage.getContent().stream()
                .map(user -> {
                    String personDocument = user.getDocumentNumber();
                    Persons person = getPersonMs.getPersonOfMsPersons(personDocument);
                    user.setPerson(person);
                    return user;
                })
                .toList();

        // Retornamos los elementos con la paginación y filtro aplicado.
        logger.info("Listado de personas obtenido con toda la data requerida");
        return new PageImpl<>(userDtos, pageable, userPage.getTotalElements());


    }

    @Override
    public ResponseWrapper<User> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<User> update(Long id, UpdateUserDto sportsMan) {
        return null;
    }

    @Override
    public ResponseWrapper<User> delete(Long id) {
        return null;
    }
}
