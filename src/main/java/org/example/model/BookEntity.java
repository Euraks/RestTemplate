package org.example.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookEntity {
    private UUID uuid;
    private String bookText;
    private List<TagEntity> tagEntities;


    public BookEntity() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getBookText() {
        return bookText;
    }

    public void setBookText(String bookText) {
        this.bookText = bookText;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntity)) return false;
        BookEntity that = (BookEntity) o;
        return Objects.equals( getBookText(), that.getBookText() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getBookText() );
    }

    @Override
    public String toString() {
        return "BookEntity{" +
                "uuid=" + uuid +
                ", bookText='" + bookText + '\'' +
                ", tagEntities=" + tagEntities +
                '}';
    }
}
