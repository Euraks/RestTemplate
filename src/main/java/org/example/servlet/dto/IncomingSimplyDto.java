package org.example.servlet.dto;

import org.example.servlet.dto.intereace.Descriprion;
import org.example.servlet.dto.intereace.ID;

import java.util.UUID;

public class IncomingSimplyDto implements ID, Descriprion {

    private UUID uuid;
    private String description;

    public IncomingSimplyDto() {
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
