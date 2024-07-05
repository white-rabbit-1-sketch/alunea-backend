package me.silvermail.backend.repository;

import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactAliasRepository extends CrudRepository<ContactAlias, String> {
    Optional<ContactAlias> findById(String id);
    List<ContactAlias> findAllByUserId(String userId);
    ContactAlias findByContactIdAndMailboxAliasId(String contactId, String mailboxAliasId);
}