package com.sebastian.liceoyarah.ms.professors.repositories;

import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long>, JpaSpecificationExecutor<Professor> {

    @Query("SELECT p FROM Professor p WHERE p.documentNumber = :documentNumber")
    Optional<Professor> findByNumberDocument(@Param("documentNumber") String documentNumber);

}
