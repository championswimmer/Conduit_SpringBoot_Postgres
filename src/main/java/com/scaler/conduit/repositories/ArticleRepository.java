package com.scaler.conduit.repositories;

import com.scaler.conduit.entities.ArticleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    ArticleEntity findArticleEntityBySlug(@Param("slug") String slug);

    @Query(value = "select * from articles a where a.author_id in (select users_id from user_followers uf where" +
            " followers_id = ?1 ) order by created_at desc",
            nativeQuery = true)
    List<ArticleEntity> findArticleFeed(long userId, Pageable pageable);

    @Query(value = "select distinct(id), created_at, updated_at, body, description, slug, title, author_id from (select a.* from" +
            " articles a inner join users u on a.author_id = u.id left join articles_tags at2 on a.id = at2.articles_id " +
            "full outer join tags t on at2.tags_id = t.id full outer join favourites f on f.favorited_id = a.id where " +
            "t.name = :tag or u.username = :author or f.fans_id = (select id from users where username = :fav)) dd order by created_at desc",
            nativeQuery = true)
    List<ArticleEntity> findByAuthorOrTagOrFavorited(
            @Param("tag") String tag,
            @Param("author") String author,
            @Param("fav") String favorited,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = "delete from articles a where a.slug = ?1", nativeQuery = true)
    void deleteBySlug(String slug);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM favourites WHERE favorite_id = ?2 AND fans_id = ?1", nativeQuery = true)
    void unfavoriteArticle(Long userId, Long articleId);
}
