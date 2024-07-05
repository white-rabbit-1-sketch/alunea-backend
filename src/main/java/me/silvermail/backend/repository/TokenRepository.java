package me.silvermail.backend.repository;

import me.silvermail.backend.entity.token.AbstractToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<AbstractToken, String> {
    Optional<AbstractToken> findById(String id);
}