package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.SimpleEntity;
import org.example.repository.SimpleEntityRepository;
import org.example.repository.mapper.SimpleResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class SimpleEntityRepositoryImpl implements SimpleEntityRepository {

    private SimpleResultSetMapper resultSetMapper;
    private final ConnectionManager connectionManager = (ConnectionManager) new HikariCPDataSource().getConnection();

    @Override
    public SimpleEntity findById(UUID id) {
        // Здесь используем try with resources
        try(Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("");
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public SimpleEntity findAll() {
        return null;
    }

    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) {
        try(Connection connection = connectionManager.getConnection()) {
            String sql = "INSERT INTO simpleentity (uuid, description) Values (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject( 1,simpleEntity.getUuid() );
            preparedStatement.setString( 2,simpleEntity.getDescription() );
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.map(resultSet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
