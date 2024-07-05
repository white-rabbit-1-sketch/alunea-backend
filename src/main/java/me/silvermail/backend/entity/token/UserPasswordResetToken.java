package me.silvermail.backend.entity.token;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("user-password-reset")
public class UserPasswordResetToken extends AbstractToken {

}