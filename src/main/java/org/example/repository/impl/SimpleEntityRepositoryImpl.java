package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.repository.mapper.SimpleResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class SimpleEntityRepositoryImpl implements Repository<SimpleEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger( SimpleEntityRepositoryImpl.class );

    private static final String FIND_BY_ID_SQL = "SELECT uuid, description FROM simpleentity WHERE uuid=?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM simpleentity WHERE uuid=?";
    private static final String FIND_ALL_SQL = "SELECT * FROM simpleentity";
    private static final String SAVE_SQL = "INSERT INTO simpleentity (uuid, description) VALUES (?, ?) " +
            "ON CONFLICT (uuid) DO UPDATE SET description = EXCLUDED.description";
    private final String DELETE_ALL_SQL = "DELETE FROM simpleentity";

    private final ConnectionManager connectionManager;

    public SimpleEntityRepositoryImpl(ConnectionManager testConnectionManager) {
        connectionManager = testConnectionManager;
    }

    @Override
    public Optional<SimpleEntity> findById(UUID uuid) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( FIND_BY_ID_SQL )){
            preparedStatement.setObject( 1, uuid );
            ResultSet resultSet = preparedStatement.executeQuery();
            List<SimpleEntity> simpleEntityList = getSimpleEntiysList( resultSet );

            return simpleEntityList.isEmpty() ? Optional.empty() : Optional.of(simpleEntityList.get(0));
        } catch(SQLException e){
            LOGGER.error("Error while finding entity by ID: {}", uuid);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(UUID uuid) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( DELETE_BY_ID_SQL )){
            preparedStatement.setObject( 1, uuid );
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch(SQLException e){
            LOGGER.error( "Error while deleting entity by ID: {}", uuid, e );
            return false;
        }
    }

    @Override
    public List<SimpleEntity> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( FIND_ALL_SQL )){
            ResultSet resultSet = preparedStatement.executeQuery();
            return getSimpleEntiysList( resultSet );
        } catch(SQLException e){
            LOGGER.error( "Error while finding all entities", e );
            return Collections.emptyList();
        }
    }

    private List<SimpleEntity> getSimpleEntiysList(ResultSet resultSet) throws SQLException {
        List<SimpleEntity> simpleEntityList = new ArrayList<>();
        while (resultSet.next()) {
            SimpleEntity simpleEntity = SimpleResultSetMapper.INSTANCE.map( resultSet );
            simpleEntityList.add( simpleEntity );
        }
        return simpleEntityList;
    }

    @Override
    public Optional<SimpleEntity> save(SimpleEntity simpleEntity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( SAVE_SQL )){
            preparedStatement.setObject( 1, simpleEntity.getUuid() );
            preparedStatement.setString( 2, simpleEntity.getDescription() );
            preparedStatement.executeUpdate();
            return Optional.of(simpleEntity);
        } catch(SQLException e){
            LOGGER.error( "Error while saving entity: {}", simpleEntity, e );
            return Optional.empty();
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_SQL)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error("Error while clearing all entities", e);
            throw new RuntimeException("Error while clearing all entities", e);
        }
    }

}
