package com.sebastian.liceoyarah.ms.professors.services;

import com.sebastian.liceoyarah.ms.professors.common.utils.ResponseWrapper;
import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.create.CreateProfessorDto;
import com.sebastian.liceoyarah.ms.professors.entities.dtos.update.UpdateProfessorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfessorService {

    ResponseWrapper<Professor> create(CreateProfessorDto professor);
    Page<Professor> findAll(String search, Pageable pageable);
    ResponseWrapper<Professor> findById(Long id);
    ResponseWrapper<Professor> update(Long id, UpdateProfessorDto professor);
    ResponseWrapper<Professor> delete(Long id);
    ResponseWrapper<Professor> findByDocument(Long documentNumber);

}
