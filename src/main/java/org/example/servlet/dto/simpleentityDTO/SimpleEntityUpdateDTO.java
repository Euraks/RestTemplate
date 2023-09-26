package org.example.servlet.dto.simpleentityDTO;

import java.util.UUID;

public class SimpleEntityUpdateDTO {
    
    private UUID uuid;
    private String description;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
