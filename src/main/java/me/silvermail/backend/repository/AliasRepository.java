package me.silvermail.backend.repository;

import me.silvermail.backend.entity.alias.AbstractAlias;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AliasRepository extends CrudRepository<AbstractAlias, String> {
    Optional<AbstractAlias> findById(String id);
    AbstractAlias findByDomainIdAndRecipient(String domainId, String recipient);
    AbstractAlias findByValue(String value);

    @Query("SELECT a FROM #{#entityName} a WHERE a.value IN :valueList")
    List<AbstractAlias> findByValueList(
            @Param("valueList") List<String> valueList
    );
}