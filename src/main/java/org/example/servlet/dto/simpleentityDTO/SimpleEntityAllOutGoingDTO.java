package org.example.servlet.dto.simpleentityDTO;

import org.example.model.SimpleEntity;

import java.util.List;

public class SimpleEntityAllOutGoingDTO {
    private List<SimpleEntity> simpleEntityList;

    public List<SimpleEntity> getSimpleEntityList() {
        return simpleEntityList;
    }

    public void setSimpleEntityList(List<SimpleEntity> simpleEntityList) {
        this.simpleEntityList = simpleEntityList;
    }
}


