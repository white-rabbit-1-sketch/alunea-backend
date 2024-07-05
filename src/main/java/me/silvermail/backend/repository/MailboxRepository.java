package me.silvermail.backend.repository;

import me.silvermail.backend.entity.Mailbox;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailboxRepository extends CrudRepository<Mailbox, String> {
    Optional<Mailbox> findById(String id);
    Mailbox findByEmail(String email);
    Mailbox findByUserIdAndEmail(String userId, String email);
    List<Mailbox> findAllByUserId(String userId);
}