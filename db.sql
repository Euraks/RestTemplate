CREATE TABLE IF NOT EXISTS SimpleEntity
(
    uuid        uuid PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS AuthorEntity
(
    id         uuid PRIMARY KEY,
    authorName TEXT
);

CREATE TABLE IF NOT EXISTS Article
(
    id        uuid PRIMARY KEY,
    author_id uuid NOT NULL,
    text      TEXT NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES AuthorEntity (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BookEntity
(
    uuid     UUID PRIMARY KEY,
    bookText TEXT
);

CREATE TABLE IF NOT EXISTS TagEntity
(
    uuid    UUID PRIMARY KEY,
    tagName TEXT
);

CREATE TABLE IF NOT EXISTS Book_Tag
(
    book_uuid UUID,
    tag_uuid  UUID,
    PRIMARY KEY (book_uuid, tag_uuid),
    FOREIGN KEY (book_uuid) REFERENCES BookEntity (uuid) ON DELETE CASCADE,
    FOREIGN KEY (tag_uuid) REFERENCES TagEntity (uuid) ON DELETE CASCADE
);


