package org.sebastian.liceoyarah.ms.users.services.impl;

import feign.FeignException;
import org.sebastian.liceoyarah.ms.users.clients.dtos.Persons;
import org.sebastian.liceoyarah.ms.users.clients.requests.GetPersonMs;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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

        //? Validar no repetición de correo
        //TODO ...

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

        logger.info("Resultado => ", personData.getFirstName());

        //? Generar username
        //TODO ...

        //* Validar que no se repita username
        //TODO ...

        //? Generar contraseña
        //TODO ...

        //* Encriptar la contraseña
        //TODO ...

        //? Guardar usuario
        //TODO ...

        //? Enviar email de credenciales
        //TODO ...

        return null;

    }

    @Override
    public Page<User> findAll(String search, Pageable pageable) {
        return null;
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
