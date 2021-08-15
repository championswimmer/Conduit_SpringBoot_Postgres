package com.scaler.conduit.repositories;

import com.scaler.conduit.entities.ArticleEntity;
import com.scaler.conduit.entities.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Long> {

    Page findArticleEntitiesByAuthor_Username(String username, Pageable pageable);

    Page findArticleEntitiesByTagsIn(List<TagEntity> tagEntities, Pageable pageable);

    ArticleEntity findArticleEntityBySlug(String slug);


}
