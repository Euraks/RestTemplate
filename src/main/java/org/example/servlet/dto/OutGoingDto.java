package org.example.servlet.dto;

import org.example.servlet.dto.intereace.Descriprion;
import org.example.servlet.dto.intereace.ID;

import java.util.UUID;

public class OutGoingDto implements ID, Descriprion {

    private UUID uuid;
    private String description;

    public OutGoingDto() {
    }

    public OutGoingDto(UUID uuid, String description) {
        this.uuid = uuid;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public UUID getID() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
