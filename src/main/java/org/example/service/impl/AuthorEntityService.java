package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuthorEntityService implements Service<AuthorEntity, UUID> {

    private AuthorEntityRepositoryImpl repository = new AuthorEntityRepositoryImpl();

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return repository.save( authorEntity );
    }

    @Override
    public AuthorEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<AuthorEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById( uuid );
    }

    @Override
    public void update(AuthorEntity authorEntity) {
        repository.update( authorEntity );
    }

    public static void main(String[] args) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setAuthorName( "Author 1" );
        List<Article> articleList = Arrays.asList( new Article( "Article 1" ),
                new Article( "Article 2" ),
                new Article( "Article 3" ),
                new Article( "Article 4" ) );
        authorEntity.setInnerEntityList( articleList );

        AuthorEntityService service = new AuthorEntityService();
        service.save( authorEntity );
    }
}
