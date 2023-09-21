package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.mapper.TagResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagRepositoryImpl implements Repository<TagEntity, UUID> {

    private static final Logger LOGGER = Logger.getLogger( TagRepositoryImpl.class.getName() );


    private final ConnectionManager connectionManager = new HikariCPDataSource();

    @Override
    public TagEntity findById(UUID uuid) {
        TagEntity tagEntity = null;
        List<BookEntity> bookEntities = new ArrayList<>();

        String bookQuery = "SELECT * FROM tagentity WHERE uuid = ?";
        String tagQuery = "SELECT t.* FROM bookentity t INNER JOIN Book_Tag bt ON t.uuid = bt.tag_uuid WHERE bt.book_uuid = ?";

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
                bookStatement.setObject(1, uuid);
                try (ResultSet rs = bookStatement.executeQuery()) {
                    if (rs.next()) {
                        tagEntity = TagResultSetMapper.INSTANCE.map(rs);
                    }
                }
            }

            if (tagEntity != null) {
                try (PreparedStatement tagStatement = connection.prepareStatement(tagQuery)) {
                    tagStatement.setObject(1, uuid);
                    try (ResultSet rs = tagStatement.executeQuery()) {
                        while (rs.next()) {
                            BookEntity bookEntity = new BookEntity();
                            bookEntity.setUuid((UUID) rs.getObject("uuid"));
                            bookEntity.setBookText(rs.getString("bookText"));
                            bookEntities.add(bookEntity);
                        }
                    }
                }
                tagEntity.setBookEntities(bookEntities);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tagEntity;
    }


    public boolean deleteById(UUID uuid) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Book_Tag WHERE tag_uuid = ?")) {
                preparedStatement.setObject(1, uuid);
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM tagentity WHERE uuid = ?")) {
                preparedStatement.setObject(1, uuid);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    LOGGER.log( Level.SEVERE, "Failed to rollback transaction", rollbackException);
                }
            }
            LOGGER.log(Level.SEVERE, "SQLException encountered while deleting", e);
            return false;

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeException) {
                    LOGGER.log(Level.WARNING, "Failed to close the connection", closeException);
                }
            }
        }
    }



    @Override
    public List<TagEntity> findAll() throws SQLException {
        String selectTagSql = "SELECT * FROM TagEntity";
        String selectBookTagRelationSql = "SELECT * FROM Book_Tag WHERE tag_uuid = ?";
        String selectBookSql = "SELECT * FROM BookEntity WHERE uuid = ?";

        List<TagEntity> tagEntities = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement tagStatement = connection.prepareStatement(selectTagSql)) {
                ResultSet tagRs = tagStatement.executeQuery();
                while (tagRs.next()) {
                    TagEntity tagEntity = new TagEntity();
                    tagEntity.setUuid((UUID) tagRs.getObject("uuid"));
                    tagEntity.setTagName(tagRs.getString("tagName"));

                    List<BookEntity> bookEntities = new ArrayList<>();
                    try (PreparedStatement bookTagStatement = connection.prepareStatement(selectBookTagRelationSql)) {
                        bookTagStatement.setObject(1, tagEntity.getUuid());
                        ResultSet bookTagRs = bookTagStatement.executeQuery();
                        while (bookTagRs.next()) {
                            UUID bookUuid = (UUID) bookTagRs.getObject("book_uuid");

                            try (PreparedStatement bookStatement = connection.prepareStatement(selectBookSql)) {
                                bookStatement.setObject(1, bookUuid);
                                ResultSet bookRs = bookStatement.executeQuery();
                                if (bookRs.next()) {
                                    BookEntity bookEntity = new BookEntity();
                                    bookEntity.setUuid(bookUuid);
                                    bookEntity.setBookText(bookRs.getString("bookText"));
                                    bookEntities.add(bookEntity);
                                }
                            }
                        }
                    }

                    tagEntity.setBookEntities(bookEntities);
                    tagEntities.add(tagEntity);
                }
            }
        }
        return tagEntities;
    }

    public TagEntity save(TagEntity tagEntity) throws SQLException {
        String insertTagSql = "INSERT INTO TagEntity (uuid, tagName) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET tagName = EXCLUDED.tagName";
        String insertBookTagRelationSql = "INSERT INTO Book_Tag (book_uuid, tag_uuid) VALUES (?, ?) ON CONFLICT DO NOTHING";
        String updateBookEntity = "INSERT INTO BookEntity (uuid, bookText) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET bookText = EXCLUDED.bookText";

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);

            if (tagEntity.getBookEntities() != null) {
                try (PreparedStatement bookStatement = connection.prepareStatement(updateBookEntity)) {
                    for (BookEntity bookEntity : tagEntity.getBookEntities()) {
                        bookStatement.setObject(1, bookEntity.getUuid());
                        bookStatement.setString(2, bookEntity.getBookText());
                        bookStatement.addBatch();
                    }
                    bookStatement.executeBatch();
                }
            }

            try (PreparedStatement tagStatement = connection.prepareStatement(insertTagSql)) {
                tagStatement.setObject(1, tagEntity.getUuid());
                tagStatement.setString(2, tagEntity.getTagName());
                tagStatement.executeUpdate();
            }

            try (PreparedStatement bookTagStatement = connection.prepareStatement(insertBookTagRelationSql)) {
                for (BookEntity bookEntity : tagEntity.getBookEntities()) {
                    bookTagStatement.setObject(1, bookEntity.getUuid());
                    bookTagStatement.setObject(2, tagEntity.getUuid());
                    bookTagStatement.addBatch();
                }
                bookTagStatement.executeBatch();
            }

            // Commit the transaction
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection connection = connectionManager.getConnection()) {
                connection.rollback();
            }
            throw e;
        }

        return tagEntity;
    }
}
