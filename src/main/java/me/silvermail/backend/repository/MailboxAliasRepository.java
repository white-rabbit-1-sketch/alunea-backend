package me.silvermail.backend.repository;

import me.silvermail.backend.entity.alias.MailboxAlias;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailboxAliasRepository extends CrudRepository<MailboxAlias, String> {
    Optional<MailboxAlias> findById(String id);
    List<MailboxAlias> findAllByUserId(String userId);
}