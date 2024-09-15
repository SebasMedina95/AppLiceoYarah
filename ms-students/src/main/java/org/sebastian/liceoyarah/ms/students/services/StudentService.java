package org.sebastian.liceoyarah.ms.students.services;

import org.sebastian.liceoyarah.ms.students.common.utils.ResponseWrapper;
import org.sebastian.liceoyarah.ms.students.entities.Student;
import org.sebastian.liceoyarah.ms.students.entities.dtos.create.CreateStudentDto;
import org.sebastian.liceoyarah.ms.students.entities.dtos.update.UpdateStudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentService {

    ResponseWrapper<Student> create(CreateStudentDto student);
    Page<Student> findAll(String search, Pageable pageable);
    ResponseWrapper<Student> findById(Long id);
    ResponseWrapper<Student> update(Long id, UpdateStudentDto student);
    ResponseWrapper<Student> delete(Long id);
    ResponseWrapper<Student> findByDocument(Long documentNumber);

}
