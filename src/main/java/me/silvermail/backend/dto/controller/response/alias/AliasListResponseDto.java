package me.silvermail.backend.dto.controller.response.alias;

import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.entity.alias.AbstractAlias;

import java.util.List;

public class AliasListResponseDto extends BaseResponseDto {
    protected List<? extends AbstractAlias> aliasList;

    public List<? extends AbstractAlias> getAliasList() {
        return aliasList;
    }

    public void setAliasList(List<? extends AbstractAlias> aliasList) {
        this.aliasList = aliasList;
    }
}
