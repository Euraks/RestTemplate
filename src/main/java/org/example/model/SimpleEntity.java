package org.example.model;


import java.util.Objects;
import java.util.UUID;


public class SimpleEntity {
    private UUID uuid;
    private String description;

    public SimpleEntity() {
        this.uuid = UUID.randomUUID();
    }

    public SimpleEntity(String description) {
        this.uuid = UUID.randomUUID();
        this.description = description;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleEntity that)) return false;
        return Objects.equals( getDescription(), that.getDescription() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getDescription() );
    }

    @Override
    public String toString() {
        return "SimpleEntity{" +
                "id=" + uuid +
                ", description='" + description + '\'' +
                '}';
    }

}
