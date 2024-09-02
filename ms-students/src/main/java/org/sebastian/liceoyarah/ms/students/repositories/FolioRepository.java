package org.sebastian.liceoyarah.ms.students.repositories;

import org.sebastian.liceoyarah.ms.students.entities.Folio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolioRepository extends JpaRepository<Folio, Long>, JpaSpecificationExecutor<Folio> {

    @Query("SELECT f FROM Folio f WHERE f.numberFolio = :numberFolio")
    Optional<Folio> getFolioByNumber(
            @Param("numberFolio") String numberFolio
    );

    @Query("SELECT f FROM Folio f " +
            "WHERE f.status = true AND" +
            "(UPPER(f.numberFolio) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(f.resolution) LIKE UPPER(CONCAT('%', :search, '%')) OR " +
            "UPPER(f.description) LIKE UPPER(CONCAT('%', :search, '%')))")
    Page<Folio> findGeneralFolioByCriteria(
            @Param("search") String search,
            Pageable pageable
    );

}
