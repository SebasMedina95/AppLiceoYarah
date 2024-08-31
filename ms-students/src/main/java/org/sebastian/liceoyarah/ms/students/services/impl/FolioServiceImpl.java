package org.sebastian.liceoyarah.ms.students.services.impl;

import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateFolioDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.update.UpdateFolioDto;
import org.sebastian.liceoyarah.ms.students.services.FolioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FolioServiceImpl implements FolioService {
    @Override
    public ResponseWrapper<Folio> create(CreateFolioDto folio) {
        return null;
    }

    @Override
    public Page<Folio> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<Folio> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<Folio> update(Long id, UpdateFolioDto folio) {
        return null;
    }

    @Override
    public ResponseWrapper<Folio> delete(Long id) {
        return null;
    }
}
