package org.example.model;


import java.util.Objects;
import java.util.UUID;


public class SimpleEntity {
    private UUID id;
    private String description;

    public SimpleEntity() {
        this.id = UUID.randomUUID();
    }

    public SimpleEntity(String description) {
        this.id = UUID.randomUUID();
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SimpleEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleEntity)) return false;
        SimpleEntity that = (SimpleEntity) o;
        return Objects.equals( getDescription(), that.getDescription() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getDescription() );
    }
}
