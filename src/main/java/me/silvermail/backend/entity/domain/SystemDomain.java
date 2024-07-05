package me.silvermail.backend.entity.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("system")
public class SystemDomain extends AbstractDomain {
    public static final String TYPE = "system";
}