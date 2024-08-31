package org.sebastian.liceoyarah.ms.students.services;

import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateFolioDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.update.UpdateFolioDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FolioService {

    ResponseWrapper<Folio> create(CreateFolioDto folio);
    Page<Folio> findAll(String search, Pageable pageable);
    ResponseWrapper<Folio> findById(Long id);
    ResponseWrapper<Folio> update(Long id, UpdateFolioDto folio);
    ResponseWrapper<Folio> delete(Long id);

}
