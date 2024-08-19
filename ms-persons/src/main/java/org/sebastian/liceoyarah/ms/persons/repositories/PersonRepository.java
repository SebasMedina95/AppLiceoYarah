package org.sebastian.liceoyarah.ms.persons.repositories;

import org.sebastian.liceoyarah.ms.persons.entities.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    @Query("SELECT p FROM Person p WHERE UPPER(p.email) = UPPER(:email) OR p.documentNumber = :documentNumber")
    Optional<Person> getPersonByDocumentAndByEmail(
            @Param("email") String email,
            @Param("documentNumber") String documentNumber
    );

    @Query("SELECT p FROM Person p WHERE (UPPER(p.email) = UPPER(:personEmail)) " +
            "AND p.id <> :id")
    Optional<Person> getPersonByDocumentAndEmailForEdit(
            @Param("personEmail") String personEmail,
            @Param("id") Long id
    );

    //Buscador dinámico para ser usado desde otros micro servicios
    //Usaremos los números de documentos que son irrepetibles
    @Query("SELECT p.documentNumber FROM Person p " +
            "WHERE p.status = true AND" +
            "(UPPER(p.documentType) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.documentNumber) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.email) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.gender) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone1) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone2) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.address) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.neighborhood) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.description) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.civilStatus) LIKE UPPER(CONCAT('%', :search, '%')))")
    List<Long> findIdsByCriteria(
            @Param("search") String search
    );

    //Buscador dinámico para el filtro de búsqueda
    @Query("SELECT p FROM Person p " +
            "WHERE p.status = true AND" +
            "(UPPER(p.documentType) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.documentNumber) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondName) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.firstSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.secondSurname) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.email) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.gender) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone1) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.phone2) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.address) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.neighborhood) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.description) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(p.civilStatus) LIKE UPPER(CONCAT('%', :search, '%')))")
    Page<Person> findGeneralPersonsByCriteria(
            @Param("search") String search,
            Pageable pageable
    );

}
