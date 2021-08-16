package com.scaler.conduit.dtos.responses.comments;


import com.scaler.conduit.entities.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class ArticleCommentResponseWithCount {
    long commentsCount;
    List<ArticleCommentResponse.Comment> comments;

    public  static ArticleCommentResponseWithCount getFromCommentEntities(long commentsCount, List<CommentEntity> commentEntities) {
        ArticleCommentResponseWithCount articleCommentResponseWithCount = new ArticleCommentResponseWithCount(commentsCount, new ArrayList<ArticleCommentResponse.Comment>());
        List<ArticleCommentResponse.Comment> commentList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            commentList.add(ArticleCommentResponse.getFromCommentEntity(commentEntity).comment);
        }
        articleCommentResponseWithCount.getComments().addAll(commentList);
        return articleCommentResponseWithCount;
    }
}
