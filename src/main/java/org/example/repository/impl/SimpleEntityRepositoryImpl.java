package org.example.repository.impl;

import org.example.db.HikariCPDataSource;
import org.example.model.SimpleEntity;
import org.example.repository.SimpleEntityRepository;
import org.example.repository.mapper.SimpleResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class SimpleEntityRepositoryImpl implements SimpleEntityRepository {

    private SimpleResultSetMapper resultSetMapper;
    private final HikariCPDataSource connectionManager = new HikariCPDataSource();

    @Override
    public SimpleEntity findById(UUID id) {
        // Здесь используем try with resources
        try (Connection connection = connectionManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement( "" );
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetMapper.map( resultSet );

        } catch(SQLException e){
            throw new RuntimeException( e );
        }
    }

    @Override
    public boolean deleteById(UUID uuid) {
        int result;
        String sql = "DELETE FROM simpleentity\n" +
                "WHERE uuid = '" + uuid.toString() + "';";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( sql )){
            result = preparedStatement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException( e );
        }
        return result > 0;
    }

    @Override
    public List<SimpleEntity> findAll() {
        String sql = "SELECT * FROM simpleentity;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( sql )){
            ResultSet resultSet = preparedStatement.executeQuery();
            return getSimpleEntiysList( resultSet );
        } catch(SQLException e){
            throw new RuntimeException( e );
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
    public SimpleEntity save(SimpleEntity simpleEntity) {
        String sql = "INSERT INTO simpleentity (uuid, description) Values (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( sql )){
            preparedStatement.setObject( 1, simpleEntity.getUuid() );
            preparedStatement.setString( 2, simpleEntity.getDescription() );
            preparedStatement.executeUpdate();
            return simpleEntity;

        } catch(SQLException e){
            throw new RuntimeException( e );
        }
    }
}
