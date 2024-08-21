package org.sebastian.liceoyarah.ms.users.repositories;

import org.sebastian.liceoyarah.ms.users.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.documentNumber = :documentNumber AND u.id <> :id")
    Optional<User> getUserByDocumentPersonForEdit(
            @Param("documentNumber") String documentNumber,
            @Param("id") Long id
    );

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.id <> :id")
    Optional<User> getUserByEmailForEdit(
            @Param("email") String email,
            @Param("id") Long id
    );

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUserName(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.documentNumber = :documentNumber")
    Optional<User> findByNumberDocument(@Param("documentNumber") String documentNumber);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);


    //? Filtro especializado para búsqueda cuando tenemos filtro
    @Query("SELECT u FROM User u " +
            "WHERE (:search IS NULL OR :search = '' OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.documentNumber) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "OR (:personDocuments IS NULL OR u.documentNumber IN :personDocuments)")
    Page<User> findFilteredUser(
            @Param("search") String search,
            @Param("personDocuments") List<String> personDocuments,
            Pageable pageable
    );

    //? Filtro especializado para búsqueda cuando no tenemos filtro
    @Query("SELECT u FROM User u WHERE u.status = true")
    Page<User> findNoFilteredUser(Pageable pageable);

}
