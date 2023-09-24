package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.mapper.BookResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class BookRepositoryImpl implements Repository<BookEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepositoryImpl.class);

    private static final String FIND_BY_ID_SQL = "SELECT * FROM BookEntity WHERE uuid = ?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM BookEntity WHERE uuid = ?";
    private static final String FIND_ALL_SQL = "SELECT b.uuid AS book_uuid, b.bookText, t.uuid AS tag_uuid, t.tagName " +
            "FROM BookEntity b " +
            "LEFT JOIN Book_Tag bt ON b.uuid = bt.book_uuid " +
            "LEFT JOIN TagEntity t ON bt.tag_uuid = t.uuid " +
            "ORDER BY b.uuid";
    private static final String SAVE_BOOK_SQL = "INSERT INTO BookEntity (uuid, bookText) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET bookText=EXCLUDED.bookText";
    private static final String DELETE_ALL_SQL = "DELETE FROM BookEntity";
    private static final String DELETE_FROM_BOOK_TAG_SQL = "DELETE FROM Book_Tag WHERE book_uuid = ?";
    private static final String INSERT_BOOK_TAG_SQL = "INSERT INTO Book_Tag (book_uuid, tag_uuid) VALUES (?, ?) ON CONFLICT DO NOTHING";
    private static final String FIND_ALL_SQL_FOR_BOOK = "SELECT b.uuid AS book_uuid, b.bookText, t.uuid AS tag_uuid, t.tagName\n" +
            "FROM BookEntity b\n" +
            "LEFT JOIN Book_Tag bt ON b.uuid = bt.book_uuid\n" +
            "LEFT JOIN TagEntity t ON bt.tag_uuid = t.uuid\n" +
            "WHERE b.uuid = ?\n" +
            "ORDER BY b.uuid\n";

    private final ConnectionManager connectionManager;
    private final Repository<TagEntity, UUID> tagRepository;

    public BookRepositoryImpl(ConnectionManager connectionManager, Repository<TagEntity, UUID> tagRepository) {
        this.connectionManager = connectionManager;
        this.tagRepository = tagRepository;
    }

    @Override
    public Optional<BookEntity> findById(UUID uuid) {
        BookEntity bookEntity = null;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement bookStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            bookStatement.setObject(1, uuid);
            try (ResultSet rs = bookStatement.executeQuery()) {
                if (rs.next()) {
                    bookEntity = BookResultSetMapper.INSTANCE.map(rs);
                }
            }
            if (bookEntity != null) {
                List<TagEntity> tagEntities = new ArrayList<>();
                try (PreparedStatement tagStatement = connection.prepareStatement(FIND_ALL_SQL_FOR_BOOK)) {
                    tagStatement.setObject(1, uuid);
                    ResultSet tagResultSet = tagStatement.executeQuery();
                    while (tagResultSet.next()) {
                        TagEntity tagEntity = new TagEntity();
                        tagEntity.setUuid(UUID.fromString(tagResultSet.getString("tag_uuid")));
                        tagEntity.setTagName(tagResultSet.getString("tagName"));
                        tagEntities.add(tagEntity);
                    }
                }
                bookEntity.setTagEntities(tagEntities);
            }
        } catch (SQLException e) {
            LOGGER.error("Error while finding book by ID: {}", uuid, e);
        }
        return Optional.ofNullable(bookEntity);
    }


    @Override
    public boolean deleteById(UUID uuid) {
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement bookTagStatement = connection.prepareStatement(DELETE_FROM_BOOK_TAG_SQL);
                 PreparedStatement bookStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
                bookTagStatement.setObject(1, uuid);
                bookTagStatement.executeUpdate();

                bookStatement.setObject(1, uuid);
                int affectedRows = bookStatement.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();
                    return false;
                }
                connection.commit();
            }
        } catch (SQLException e) {
            LOGGER.error("Error while deleting book by ID: {}", uuid, e);
            return false;
        }
        return true;
    }

    @Override
    public List<BookEntity> findAll() {
        List<BookEntity> bookEntities = new ArrayList<>();
        HashMap<UUID, BookEntity> map = new HashMap<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID bookUuid = UUID.fromString(rs.getString("book_uuid"));
                BookEntity bookEntity = map.getOrDefault(bookUuid, new BookEntity());
                bookEntity.setUuid(bookUuid);
                bookEntity.setBookText(rs.getString("bookText"));

                if (rs.getString("tag_uuid") != null) {
                    TagEntity tagEntity = new TagEntity();
                    tagEntity.setUuid(UUID.fromString(rs.getString("tag_uuid")));
                    tagEntity.setTagName(rs.getString("tagName"));
                    if (bookEntity.getTagEntities() == null) {
                        bookEntity.setTagEntities(new ArrayList<>());
                    }
                    bookEntity.getTagEntities().add(tagEntity);
                }

                map.put(bookUuid, bookEntity);
            }

            bookEntities = new ArrayList<>(map.values());

        } catch (SQLException e) {
            LOGGER.error("Error while finding all books", e);
        }

        return bookEntities;
    }

    @Override
    public Optional<BookEntity> save(BookEntity bookEntity) throws SQLException {
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            List<TagEntity> tagEntities = bookEntity.getTagEntities();
            if (tagEntities != null) {
                for (TagEntity tag : tagEntities) {
                    tagRepository.save(tag);
                }
            }

            try (PreparedStatement bookStatement = connection.prepareStatement(SAVE_BOOK_SQL)) {
                bookStatement.setObject(1, bookEntity.getUuid());
                bookStatement.setString(2, bookEntity.getBookText());
                bookStatement.executeUpdate();
            }

            // Затем сохранение связей между книгой и тегами
            if (tagEntities != null) {
                for (TagEntity tag : tagEntities) {
                    try (PreparedStatement bookTagStatement = connection.prepareStatement(INSERT_BOOK_TAG_SQL)) {
                        bookTagStatement.setObject(1, bookEntity.getUuid());
                        bookTagStatement.setObject(2, tag.getUuid());
                        bookTagStatement.executeUpdate();
                    }
                }
            }

            connection.commit();
            return Optional.of(bookEntity);

        } catch (SQLException ex) {
            LOGGER.error("Error while saving book: {}", bookEntity, ex);
            try {
                connectionManager.getConnection().rollback();
            } catch (SQLException rollbackException) {
                LOGGER.error("Failed to rollback transaction", rollbackException);
            }
            throw ex;
        }
    }



    @Override
    public void clearAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SQL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error while clearing all books", e);
            throw new RuntimeException("Error while clearing all books", e);
        }
    }
}
