package me.silvermail.backend.repository;

import me.silvermail.backend.entity.domain.SystemDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemDomainRepository extends CrudRepository<SystemDomain, String> {
    Optional<SystemDomain> findById(String id);
}