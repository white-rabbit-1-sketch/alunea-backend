package me.silvermail.backend.entity.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("custom")
public class CustomDomain extends AbstractUserDomain {
    public static final String TYPE = "custom";
}