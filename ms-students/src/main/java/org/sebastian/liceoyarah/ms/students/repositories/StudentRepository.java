package org.sebastian.liceoyarah.ms.students.repositories;

import org.sebastian.liceoyarah.ms.students.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Query("SELECT s FROM Student s WHERE s.documentNumber = :documentNumber")
    Optional<Student> findByNumberDocument(@Param("documentNumber") String documentNumber);

}
