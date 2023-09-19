package org.example.servlet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.example.model.SimpleEntity;
import org.example.servlet.dto.intereace.Descriprion;
import org.example.servlet.dto.intereace.ID;
import org.example.servlet.simpleEntityServlets.Deserializer.CustomUUIDDeserializer;

import java.util.List;
import java.util.UUID;

public class OutGoingSimplyDto implements ID, Descriprion {


    private UUID uuid;
    private String description;
    private List<SimpleEntity> simpleEntityList;

    public OutGoingSimplyDto() {
    }

    @Override
    public String getDescription() {
        return this.description;
    }


    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public List<SimpleEntity> getSimpleEntityList() {
        return simpleEntityList;
    }

    public void setSimpleEntityList(List<SimpleEntity> simpleEntityList) {
        this.simpleEntityList = simpleEntityList;
    }
}
