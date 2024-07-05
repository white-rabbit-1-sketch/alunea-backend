package me.silvermail.backend.dto.controller.response.domain;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.domain.AbstractDomain;

public class DomainResponseDto extends BaseResponseDto {
    protected AbstractDomain domain;

    public AbstractDomain getDomain() {
        return domain;
    }

    public void setDomain(AbstractDomain domain) {
        this.domain = domain;
    }
}
