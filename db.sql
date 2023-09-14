CREATE TABLE IF NOT EXISTS SimpleEntity
(
    uuid        uuid PRIMARY KEY ,
    description varchar(255)
);

CREATE TABLE IF NOT EXISTS AuthorEntity (
                        id uuid PRIMARY KEY,
                        authorName TEXT
);

CREATE TABLE IF NOT EXISTS Article (
                         id uuid PRIMARY KEY,
                         author_id INT NOT NULL,
                         text TEXT NOT NULL,
                         CONSTRAINT fk_author FOREIGN KEY(author_id) REFERENCES AuthorEntity(id)
);