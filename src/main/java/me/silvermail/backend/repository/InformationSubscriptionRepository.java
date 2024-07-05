package me.silvermail.backend.repository;

import me.silvermail.backend.entity.InformationSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationSubscriptionRepository extends CrudRepository<InformationSubscription, String> {
    InformationSubscription findByEmail(String email);
}