package me.silvermail.backend.dto.controller.request.domain.subdomain;

import jakarta.validation.constraints.Pattern;

public class CreateSubdomainRequestDto {
    @Pattern(regexp = "^(?!-)[A-Za-z0-9-]{1,63}(?<!-)$", message = "Invalid subdomain format")
    protected String subdomain;

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }
}
