package org.example.repository.impl;

import org.example.db.HikariCPDataSource;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthorEntityRepositoryImpl<T, K> implements AuthorEntityRepository<AuthorEntity, UUID> {

    private final HikariCPDataSource connectionManager = new HikariCPDataSource();

    @Override
    public AuthorEntity findById(UUID id) {
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public List<AuthorEntity> findAll() {
        return null;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        String sql = "";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( sql )){
            return authorEntity;

        } catch(SQLException e){
            throw new RuntimeException( e );
        }
    }

    @Override
    public AuthorEntity update(AuthorEntity simpleEntity) {
        return null;
    }
}
