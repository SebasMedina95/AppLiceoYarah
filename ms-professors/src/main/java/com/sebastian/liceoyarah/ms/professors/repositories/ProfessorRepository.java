package com.sebastian.liceoyarah.ms.professors.repositories;

import com.sebastian.liceoyarah.ms.professors.entities.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long>, JpaSpecificationExecutor<Professor> {

    @Query("SELECT p FROM Professor p WHERE p.documentNumber = :documentNumber")
    Optional<Professor> findByNumberDocument(@Param("documentNumber") String documentNumber);

    @Query("SELECT p FROM Professor p WHERE p.cardTittle = :cardTittle")
    Optional<Professor> findByCardTittle(@Param("cardTittle") String cardTittle);

    @Query("SELECT p FROM Professor p WHERE (UPPER(p.cardTittle) = UPPER(:cardTitle)) " +
            "AND p.id <> :id")
    Optional<Professor> getProfessorByCardTittleForEdit(
            @Param("cardTitle") String cardTitle,
            @Param("id") Long id
    );

    //? Listado con filtrado especializado
    @Query("SELECT p FROM Professor p " +
            "WHERE p.status = true AND (" +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(p.institutionalEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.documentNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.type) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.proffesionalTittle) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.cardTittle) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.laborDay) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.core) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "OR (:personDocuments IS NULL OR p.documentNumber IN :personDocuments))")
    Page<Professor> findFilteredProfessor(
            @Param("search") String search,
            @Param("personDocuments") List<String> personDocuments,
            Pageable pageable
    );

    //? Filtro especializado para búsqueda cuando no tenemos filtro
    @Query("SELECT p FROM Professor p WHERE p.status = true")
    Page<Professor> findNoFilteredProfessor(Pageable pageable);

}
