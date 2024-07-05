package me.silvermail.backend.repository;

import me.silvermail.backend.entity.domain.AbstractDomain;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends CrudRepository<AbstractDomain, String> {
    Optional<AbstractDomain> findById(String id);
    @Query("SELECT d FROM #{#entityName} d WHERE d.domain = :domain AND d.type != 'system'")
    AbstractDomain findDomainByDomain(@Param("domain") String domain);
}