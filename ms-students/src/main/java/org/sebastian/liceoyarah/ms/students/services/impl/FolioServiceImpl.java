package org.sebastian.liceoyarah.ms.students.services.impl;

import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateFolioDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.update.UpdateFolioDto;
import org.sebastian.liceoyarah.ms.students.repositories.FolioRepository;
import org.sebastian.liceoyarah.ms.students.services.FolioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class FolioServiceImpl implements FolioService {

    static String dummiesUser = "usuario123";
    private static final Logger logger = LoggerFactory.getLogger(FolioServiceImpl.class);
    private final FolioRepository folioRepository;

    @Autowired
    public FolioServiceImpl(
            FolioRepository folioRepository
    ){
        this.folioRepository = folioRepository;
    }

    @Override
    @Transactional
    public ResponseWrapper<Folio> create(CreateFolioDto folio) {

        logger.info("Iniciando Acción - MS Students - Creación de un folio");

        try{

            //? Validemos que no se repita la persona
            String numberFolio = folio.getNumberFolio().trim().toUpperCase();
            Optional<Folio> getFolioOptional = folioRepository.getFolioByNumber(numberFolio);
            if( getFolioOptional.isPresent() )
                return new ResponseWrapper<>(null, "El número de folio ya está registrado");

            //? Pasamos hasta acá, registramos persona
            Folio newFolio = new Folio();
            newFolio.setNumberFolio(numberFolio);
            newFolio.setResolution(folio.getResolution());
            newFolio.setDescription(folio.getDescription());
            newFolio.setStatus(true); //* Por defecto entra en true
            newFolio.setUserCreated(dummiesUser); //! Ajustar cuando se implemente Security
            newFolio.setDateCreated(new Date()); //! Ajustar cuando se implemente Security
            newFolio.setUserUpdated(dummiesUser); //! Ajustar cuando se implemente Security
            newFolio.setDateUpdated(new Date()); //! Ajustar cuando se implemente Security

            logger.info("Folio creado correctamente");
            return new ResponseWrapper<>(folioRepository.save(newFolio), "Folio guardado correctamente");

        }catch (Exception ex){

            logger.error("Ocurrió un error al intentar crear un folio, detalles ...", ex);
            return new ResponseWrapper<>(null, "El folio no pudo ser creada");

        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Folio> findAll(String search, Pageable pageable) {

        logger.info("Iniciando Acción - MS Students - Obtener todos los folios paginados y con filtro");
        Page<Folio> folios = folioRepository.findGeneralFolioByCriteria(search, pageable);

        logger.info("Listado de folios obtenido");
        return folios;

    }

    @Override
    @Transactional(readOnly = true)
    public ResponseWrapper<Folio> findById(Long id) {

        logger.info("Iniciando Acción - MS Students - Obtener un folio dado su ID");

        try{

            Optional<Folio> folioOptional = folioRepository.findById(id);

            if( folioOptional.isPresent() ){
                Folio folio = folioOptional.orElseThrow();
                logger.info("Folio obtenido por su ID");
                return new ResponseWrapper<>(folio, "Folio encontrado por ID correctamente");
            }

            logger.info("El folio no pudo ser encontrada cone el ID {}", id);
            return new ResponseWrapper<>(null, "El folio no pudo ser encontrado por el ID " + id);

        }catch (Exception err) {

            logger.error("Ocurrió un error al intentar obtener folio por ID, detalles ...", err);
            return new ResponseWrapper<>(null, "El folio no pudo ser encontrado por el ID");

        }

    }

    @Override
    @Transactional
    public ResponseWrapper<Folio> update(Long id, UpdateFolioDto folio) {
        return null;
    }

    @Override
    @Transactional
    public ResponseWrapper<Folio> delete(Long id) {
        return null;
    }
}
