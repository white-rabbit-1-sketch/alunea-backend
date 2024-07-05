package me.silvermail.backend.entity.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("subdomain")
public class Subdomain extends AbstractUserDomain {
    public static final String TYPE = "subdomain";

    protected String subdomain;

    @ManyToOne
    protected SystemDomain systemDomain;

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public SystemDomain getSystemDomain() {
        return systemDomain;
    }

    public void setSystemDomain(SystemDomain systemDomain) {
        this.systemDomain = systemDomain;
    }
}