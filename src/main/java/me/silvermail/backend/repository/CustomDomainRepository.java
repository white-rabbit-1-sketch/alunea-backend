package me.silvermail.backend.repository;

import me.silvermail.backend.entity.domain.CustomDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomDomainRepository extends CrudRepository<CustomDomain, String> {
    Optional<CustomDomain> findById(String id);
    List<CustomDomain> findAllByUserId(String userId);
}