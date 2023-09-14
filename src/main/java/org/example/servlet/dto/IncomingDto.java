package org.example.servlet.dto;

import org.example.servlet.dto.intereace.Descriprion;
import org.example.servlet.dto.intereace.ID;

import java.util.UUID;

public class IncomingDto implements ID, Descriprion {

    private UUID uuid;
    private String description;

    public IncomingDto() {
    }

    @Override
    public String getDescription() {
        return this.description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
