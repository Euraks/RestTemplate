package org.example.model;

import java.util.List;

public class OneToManyEntity {
    private String name;
    private List<InnerEntity> innerEntityList;

    public OneToManyEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InnerEntity> getInnerEntityList() {
        return innerEntityList;
    }

    public void setInnerEntityList(List<InnerEntity> innerEntityList) {
        this.innerEntityList = innerEntityList;
    }
}

