package org.example.model;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TagEntity {
    private UUID uuid;
    private String tagName;
    private List<BookEntity> bookEntities;

    public TagEntity() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<BookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<BookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity)) return false;
        TagEntity tagEntity = (TagEntity) o;
        return Objects.equals( getTagName(), tagEntity.getTagName() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getTagName() );
    }

    @Override
    public String toString() {
        return "TagEntity{" +
                "uuid=" + uuid +
                ", tagName='" + tagName + '\'' +
                ", bookEntities=" + bookEntities +
                '}';
    }
}
