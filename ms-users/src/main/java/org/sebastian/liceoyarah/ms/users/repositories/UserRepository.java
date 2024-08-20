package org.sebastian.liceoyarah.ms.users.repositories;

import org.sebastian.liceoyarah.ms.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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



}
