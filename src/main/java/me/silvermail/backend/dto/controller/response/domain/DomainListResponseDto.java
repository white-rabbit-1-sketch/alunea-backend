package me.silvermail.backend.dto.controller.response.domain;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.domain.AbstractDomain;

import java.util.List;

public class DomainListResponseDto extends BaseResponseDto {
    protected List<? extends AbstractDomain> domainList;

    public List<? extends AbstractDomain> getDomainList() {
        return domainList;
    }

    public void setDomainList(List<? extends AbstractDomain> domainList) {
        this.domainList = domainList;
    }
}
