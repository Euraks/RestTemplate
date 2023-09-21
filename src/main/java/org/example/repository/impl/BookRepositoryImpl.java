package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BookRepositoryImpl implements Repository<BookEntity, UUID> {

    private final ConnectionManager connectionManager = new HikariCPDataSource();

    @Override
    public BookEntity findById(UUID uuid) {
        BookEntity bookEntity = null;
        List<TagEntity> tagEntities = new ArrayList<>();

        String bookQuery = "SELECT * FROM BookEntity WHERE uuid = ?";
        String tagQuery = "SELECT t.* FROM TagEntity t INNER JOIN Book_Tag bt ON t.uuid = bt.tag_uuid WHERE bt.book_uuid = ?";

        try (Connection connection = connectionManager.getConnection()){
            try (PreparedStatement bookStatement = connection.prepareStatement( bookQuery )){
                bookStatement.setObject( 1, uuid );
                try (ResultSet rs = bookStatement.executeQuery()){
                    if (rs.next()) {
                        bookEntity = new BookEntity();
                        bookEntity.setUuid( (UUID) rs.getObject( "uuid" ) );
                        bookEntity.setBookText( rs.getString( "bookText" ) );
                    }
                }
            }

            if (bookEntity != null) {
                try (PreparedStatement tagStatement = connection.prepareStatement( tagQuery )){
                    tagStatement.setObject( 1, uuid );
                    try (ResultSet rs = tagStatement.executeQuery()){
                        while (rs.next()) {
                            TagEntity tagEntity = new TagEntity();
                            tagEntity.setUuid( (UUID) rs.getObject( "uuid" ) );
                            tagEntity.setTagName( rs.getString( "tagName" ) );
                            tagEntities.add( tagEntity );
                        }
                    }
                }
                bookEntity.setTagEntities( tagEntities );
            }
        } catch(Exception e){
            e.printStackTrace();
            // Handle exception
        }

        return bookEntity;
    }


    public boolean deleteById(UUID uuid) {
        Connection connection = null;
        try{
            connection = connectionManager.getConnection();
            connection.setAutoCommit( false );

            // Удаление связанных записей в таблице Book_Tag
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Book_Tag WHERE book_uuid = ?" )){
                preparedStatement.setObject( 1, uuid );
                preparedStatement.executeUpdate();
            }

            // Удаление записи в таблице BookEntity
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM BookEntity WHERE uuid = ?" )){
                preparedStatement.setObject( 1, uuid );
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    return false;
                }
            }

            connection.commit();
            return true;

        } catch(SQLException e){
            if (connection != null) {
                try{
                    connection.rollback();
                } catch(SQLException rollbackException){
                    // обработка исключения
                }
            }
            // обработка исключения
            return false;
        } finally{
            if (connection != null) {
                try{
                    connection.close();
                } catch(SQLException closeException){
                    // обработка исключения
                }
            }
        }
    }


    @Override
    public List<BookEntity> findAll() {
        List<BookEntity> bookEntities = new ArrayList<>();
        HashMap<UUID, BookEntity> map = new HashMap<>();
        String sql = "SELECT b.uuid AS book_uuid, b.bookText, t.uuid AS tag_uuid, t.tagName " +
                "FROM BookEntity b " +
                "LEFT JOIN Book_Tag bt ON b.uuid = bt.book_uuid " +
                "LEFT JOIN TagEntity t ON bt.tag_uuid = t.uuid " +
                "ORDER BY b.uuid";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement( sql )){

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID bookUuid = UUID.fromString( rs.getString( "book_uuid" ) );
                BookEntity bookEntity = map.getOrDefault( bookUuid, new BookEntity() );
                bookEntity.setUuid( bookUuid );
                bookEntity.setBookText( rs.getString( "bookText" ) );

                if (rs.getString( "tag_uuid" ) != null) {
                    TagEntity tagEntity = new TagEntity();
                    tagEntity.setUuid( UUID.fromString( rs.getString( "tag_uuid" ) ) );
                    tagEntity.setTagName( rs.getString( "tagName" ) );
                    if (bookEntity.getTagEntities() == null) {
                        bookEntity.setTagEntities( new ArrayList<>() );
                    }
                    bookEntity.getTagEntities().add( tagEntity );
                }

                map.put( bookUuid, bookEntity );
            }

            bookEntities = new ArrayList<>( map.values() );

        } catch(Exception e){
            e.printStackTrace();
        }

        return bookEntities;
    }

    @Override
    public BookEntity save(BookEntity bookEntity) throws SQLException {
        Connection connection = null;

        try{
            connection = connectionManager.getConnection();
            connection.setAutoCommit( false );


            String bookInsertQuery = "INSERT INTO BookEntity (uuid, bookText) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET bookText=EXCLUDED.bookText;;";
            try (PreparedStatement ps = connection.prepareStatement( bookInsertQuery )){
                ps.setObject( 1, bookEntity.getUuid() );
                ps.setString( 2, bookEntity.getBookText() );
                ps.executeUpdate();
            }


            List<TagEntity> tagEntities = bookEntity.getTagEntities();
            if (tagEntities != null) {
                for (TagEntity tag : tagEntities) {
                    String tagInsertQuery = "INSERT INTO TagEntity (uuid, tagName) VALUES (?, ?) ON CONFLICT (uuid) DO UPDATE SET tagName=EXCLUDED.tagName;";
                    try (PreparedStatement ps = connection.prepareStatement( tagInsertQuery )){
                        ps.setObject( 1, tag.getUuid() );
                        ps.setString( 2, tag.getTagName() );
                        ps.executeUpdate();
                    }

                    String bookTagInsertQuery = "INSERT INTO Book_Tag (book_uuid, tag_uuid) VALUES (?, ?) ON CONFLICT (book_uuid, tag_uuid) DO NOTHING;";
                    try (PreparedStatement ps = connection.prepareStatement( bookTagInsertQuery )){
                        ps.setObject( 1, bookEntity.getUuid() );
                        ps.setObject( 2, tag.getUuid() );
                        ps.executeUpdate();
                    }
                }
            }

            connection.commit();
            return bookEntity;

        } catch(SQLException ex){
            if (connection != null) {
                connection.rollback();
            }
            throw ex;

        } finally{
            if (connection != null) {
                connection.setAutoCommit( true );
                connection.close();
            }
        }
    }
}
