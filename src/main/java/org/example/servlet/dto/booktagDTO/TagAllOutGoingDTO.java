package org.example.servlet.dto.booktagDTO;

import org.example.model.TagEntity;

import java.util.List;

public class TagAllOutGoingDTO {
    List<TagEntity> tagEntities;

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }
}
