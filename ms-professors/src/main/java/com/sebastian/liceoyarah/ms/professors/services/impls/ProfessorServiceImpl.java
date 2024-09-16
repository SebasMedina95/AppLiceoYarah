package com.sebastian.liceoyarah.ms.professors.services.impls;

import com.sebastian.liceoyarah.ms.professors.common.utils.ResponseWrapper;
import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.create.CreateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.update.UpdateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.services.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ProfessorServiceImpl implements ProfessorService {
    @Override
    public ResponseWrapper<Professor> create(CreateProfessorDto professor) {
        return null;
    }

    @Override
    public Page<Professor> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<Professor> findById(Long id) {
        return null;
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
}
