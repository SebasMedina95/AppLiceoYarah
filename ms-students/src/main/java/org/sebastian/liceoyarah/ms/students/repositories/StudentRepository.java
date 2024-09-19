package org.sebastian.liceoyarah.ms.students.repositories;

import org.sebastian.liceoyarah.ms.students.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("SELECT s FROM Student s WHERE s.documentNumber = :documentNumber")
    Optional<Student> findByNumberDocument(@Param("documentNumber") String documentNumber);

    //? Listado con filtrado especializado
    @Query("SELECT s FROM Student s " +
            "WHERE (:search IS NULL OR :search = '' OR " +
            "LOWER(s.isbttsCode) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.documentNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.yearInscription) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "OR (:personDocuments IS NULL OR s.documentNumber IN :personDocuments)")
    Page<Student> findFilteredStudent(
            @Param("search") String search,
            @Param("personDocuments") List<String> personDocuments,
            Pageable pageable
    );

    //? Filtro especializado para búsqueda cuando no tenemos filtro
    @Query("SELECT s FROM Student s WHERE s.status = true")
    Page<Student> findNoFilteredStudent(Pageable pageable);

}
