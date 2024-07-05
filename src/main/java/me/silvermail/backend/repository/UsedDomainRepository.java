package me.silvermail.backend.repository;

import me.silvermail.backend.entity.UsedDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedDomainRepository extends CrudRepository<UsedDomain, String> {
    UsedDomain findByDomain(String domain);
}