package me.silvermail.backend.entity.token;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("user-auth")
public class UserAuthToken extends AbstractToken {

}