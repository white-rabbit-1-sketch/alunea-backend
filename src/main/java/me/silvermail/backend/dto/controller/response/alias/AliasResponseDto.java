package me.silvermail.backend.dto.controller.response.alias;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.alias.AbstractAlias;

public class AliasResponseDto extends BaseResponseDto {
    protected AbstractAlias alias;

    public AbstractAlias getAlias() {
        return alias;
    }

    public void setAlias(AbstractAlias alias) {
        this.alias = alias;
    }
}
