package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.repository.mapper.SimpleResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SimpleEntityRepositoryImpl implements Repository<SimpleEntity, UUID> {

    private static final String FIND_BY_ID_SQL = "SELECT uuid, description FROM SimpleEntity WHERE uuid=?";
    private static final String DELETE_BY_ID_SQL = "DELETE FROM SimpleEntity WHERE uuid=?";
    private static final String FIND_ALL_SQL = "SELECT * FROM SimpleEntity";
    private static final String SAVE_SQL = "INSERT INTO SimpleEntity (uuid, description) VALUES (?, ?) " +
            "ON CONFLICT (uuid) DO UPDATE SET description = EXCLUDED.description";
    private static final String DELETE_ALL_SQL = "DELETE FROM SimpleEntity";

    private final ConnectionManager connectionManager;

    public SimpleEntityRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<SimpleEntity> findById(UUID uuid) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setObject(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<SimpleEntity> simpleEntityList = getSimpleEntitiesList(resultSet);

            return simpleEntityList.isEmpty() ? Optional.empty() : Optional.of(simpleEntityList.get(0));
        }
    }

    @Override
    public boolean deleteById(UUID uuid) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setObject(1, uuid);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        }
    }

    @Override
    public List<SimpleEntity> findAll() throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return getSimpleEntitiesList(resultSet);
        }
    }

    private List<SimpleEntity> getSimpleEntitiesList(ResultSet resultSet) throws SQLException {
        List<SimpleEntity> simpleEntityList = new ArrayList<>();
        while (resultSet.next()) {
            SimpleEntity simpleEntity = SimpleResultSetMapper.INSTANCE.map(resultSet);
            simpleEntityList.add(simpleEntity);
        }
        return simpleEntityList;
    }

    @Override
    public Optional<SimpleEntity> save(SimpleEntity simpleEntity) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setObject(1, simpleEntity.getUuid());
            preparedStatement.setString(2, simpleEntity.getDescription());
            preparedStatement.executeUpdate();
            return Optional.of(simpleEntity);
        }
    }

    @Override
    public void clearAll() throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_SQL)) {
            preparedStatement.executeUpdate();
        }
    }
}
