package org.example.servlet.dto.AuthorEntityDTO;

import org.example.model.AuthorEntity;

import java.util.ArrayList;
import java.util.List;

public class AuthorEntityAllOutGoingDTO {
    private List<AuthorEntity> authorEntityList;

    public List<AuthorEntity> getAuthorEntityList() {
        if (this.authorEntityList == null) {
            this.authorEntityList = new ArrayList<>();
        }
        return this.authorEntityList;
    }

    public void setAuthorEntityList(List<AuthorEntity> authorEntityList) {
        this.authorEntityList = authorEntityList;
    }
}
