package org.example.repository.impl;
import org.example.db.ConnectionManager;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.mapper.BookResultSetMapper;
import org.example.repository.mapper.TagResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
//
//public class TagRepositoryImpl implements Repository<TagEntity, UUID> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(TagRepositoryImpl.class);
//
//    private static final String FIND_BY_ID_SQL = "SELECT * FROM tagentity WHERE uuid = ?";
//    private static final String DELETE_BY_ID_SQL = "DELETE FROM tagentity WHERE uuid = ?";
//    private static final String FIND_ALL_SQL = "SELECT * FROM TagEntity";
//    private static final String SAVE_TAG_SQL = "INSERT INTO TagEntity (uuid, tagName) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET tagName = EXCLUDED.tagName";
//    private static final String DELETE_ALL_SQL = "DELETE FROM tagentity";
//    private static final String DELETE_FROM_BOOK_TAG_SQL = "DELETE FROM Book_Tag WHERE tag_uuid = ?";
//    private static final String SAVE_BOOK_SQL = "INSERT INTO BookEntity (uuid, bookText) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET bookText = EXCLUDED.bookText";
//    private static final String INSERT_BOOK_TAG_SQL = "INSERT INTO Book_Tag (book_uuid, tag_uuid) VALUES (?, ?) ON CONFLICT DO NOTHING";
//    private static final String FIND_BOOKS_BY_TAG_SQL = "SELECT b.* FROM BookEntity b JOIN Book_Tag bt ON b.uuid = bt.book_uuid WHERE bt.tag_uuid = ?";
//    private static final String FIND_ALL_BOOKS_BY_TAGS_SQL = "SELECT b.*, bt.tag_uuid FROM BookEntity b JOIN Book_Tag bt ON b.uuid = bt.book_uuid";
//
//    private final ConnectionManager connectionManager;
//
//    public TagRepositoryImpl(ConnectionManager connectionManager) {
//        this.connectionManager = connectionManager;
//    }
//
//    @Override
//    public Optional<TagEntity> findById(UUID uuid) {
//        TagEntity tagEntity = null;
//        try (Connection connection = connectionManager.getConnection();
//             PreparedStatement tagStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
//            tagStatement.setObject(1, uuid);
//            try (ResultSet rs = tagStatement.executeQuery()) {
//                if (rs.next()) {
//                    tagEntity = TagResultSetMapper.INSTANCE.map(rs);
//                }
//            }
//            if (tagEntity != null) {
//                try (PreparedStatement bookStatement = connection.prepareStatement(FIND_BOOKS_BY_TAG_SQL)) {
//                    bookStatement.setObject(1, uuid);
//                    ResultSet bookResultSet = bookStatement.executeQuery();
//                    List<BookEntity> bookEntities = new ArrayList<>();
//                    while (bookResultSet.next()) {
//                        BookEntity bookEntity = BookResultSetMapper.INSTANCE.map(bookResultSet);
//                        bookEntities.add(bookEntity);
//                    }
//                    tagEntity.setBookEntities(bookEntities);
//                }
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Error while finding tag by ID: {}", uuid, e);
//        }
//        return Optional.ofNullable(tagEntity);
//    }
//
//
//    @Override
//    public boolean deleteById(UUID uuid) {
//        try (Connection connection = connectionManager.getConnection()) {
//            connection.setAutoCommit(false);
//            try (PreparedStatement bookTagStatement = connection.prepareStatement(DELETE_FROM_BOOK_TAG_SQL);
//                 PreparedStatement tagStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
//                bookTagStatement.setObject(1, uuid);
//                bookTagStatement.executeUpdate();
//
//                tagStatement.setObject(1, uuid);
//                int affectedRows = tagStatement.executeUpdate();
//                if (affectedRows == 0) {
//                    connection.rollback();
//                    return false;
//                }
//                connection.commit();
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Error while deleting tag by ID: {}", uuid, e);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public List<TagEntity> findAll() throws SQLException {
//        List<TagEntity> tagEntities = new ArrayList<>();
//        Map<UUID, TagEntity> tagMap = new HashMap<>();
//        try (Connection connection = connectionManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL);
//             ResultSet resultSet = statement.executeQuery()) {
//            while (resultSet.next()) {
//                TagEntity tagEntity = TagResultSetMapper.INSTANCE.map(resultSet);
//                tagEntities.add(tagEntity);
//                tagMap.put(tagEntity.getUuid(), tagEntity);
//            }
//            try (PreparedStatement bookStatement = connection.prepareStatement(FIND_ALL_BOOKS_BY_TAGS_SQL)) {
//                ResultSet bookResultSet = bookStatement.executeQuery();
//                while (bookResultSet.next()) {
//                    UUID tagUuid = (UUID) bookResultSet.getObject("tag_uuid");
//                    TagEntity relatedTag = tagMap.get(tagUuid);
//                    if (relatedTag != null) {
//                        BookEntity bookEntity = BookResultSetMapper.INSTANCE.map(bookResultSet);
//                        if (relatedTag.getBookEntities() == null) {
//                            relatedTag.setBookEntities(new ArrayList<>());
//                        }
//                        relatedTag.getBookEntities().add(bookEntity);
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            LOGGER.error("Error while finding all tags", e);
//            throw e;
//        }
//        return tagEntities;
//    }
//
//
//    @Override
//    public Optional<TagEntity> save(TagEntity tagEntity) throws SQLException {
//        try (Connection connection = connectionManager.getConnection()) {
//            connection.setAutoCommit(false);
//
//            try (PreparedStatement tagStatement = connection.prepareStatement(SAVE_TAG_SQL)) {
//                tagStatement.setObject(1, tagEntity.getUuid());
//                tagStatement.setString(2, tagEntity.getTagName());
//                tagStatement.executeUpdate();
//            }
//
//            if (tagEntity.getBookEntities() != null && !tagEntity.getBookEntities().isEmpty()) {
//                try (PreparedStatement bookStatement = connection.prepareStatement(SAVE_BOOK_SQL);
//                     PreparedStatement bookTagStatement = connection.prepareStatement(INSERT_BOOK_TAG_SQL)) {
//                    for (BookEntity bookEntity : tagEntity.getBookEntities()) {
//                        bookStatement.setObject(1, bookEntity.getUuid());
//                        bookStatement.setString(2, bookEntity.getBookText());
//                        bookStatement.addBatch();
//
//                        bookTagStatement.setObject(1, bookEntity.getUuid());
//                        bookTagStatement.setObject(2, tagEntity.getUuid());
//                        bookTagStatement.addBatch();
//                    }
//                    bookStatement.executeBatch();
//                    bookTagStatement.executeBatch();
//                }
//            }
//
//            connection.commit();
//            return Optional.of(tagEntity);
//
//        } catch (SQLException e) {
//            LOGGER.error("Error while saving tag: {}", tagEntity, e);
//            try {
//                connectionManager.getConnection().rollback();
//            } catch (SQLException rollbackException) {
//                LOGGER.error("Failed to rollback transaction", rollbackException);
//            }
//            throw e;
//        }
//    }
//
//    @Override
//    public void clearAll() {
//        try (Connection connection = connectionManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SQL)) {
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            LOGGER.error("Error while clearing all tags", e);
//            throw new RuntimeException("Error while clearing all tags", e);
//        }
//    }
//}
