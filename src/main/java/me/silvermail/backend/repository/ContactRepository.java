package me.silvermail.backend.repository;

import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.alias.AbstractAlias;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
    Optional<Contact> findById(String id);
    Contact findByUserIdAndEmail(String userId, String email);
    List<Contact> findAllByUserId(String userId);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND c.email IN :emailList")
    List<Contact> findByUserIdAndEmailList(
            @Param("userId") String userId,
            @Param("emailList") List<String> emailList
    );
}