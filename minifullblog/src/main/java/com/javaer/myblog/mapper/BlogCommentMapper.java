package com.javaer.myblog.mapper;

import com.javaer.myblog.entity.BlogComment;

import java.util.List;

public interface BlogCommentMapper {
    void save(BlogComment blogComment);

    BlogComment getCommentByCommentId(Long commentId);

    List<BlogComment> getCommentByBlogId(Long blogId);

    List<BlogComment> findByBlogIdAndParentCommentNull(Long blogId);

    List<BlogComment> findReplyCommentsByParentCommentId(Long commentId);

    int deleteBatch(Integer[] ids);

    int getTotalComments();

    List<BlogComment> getAllComment();
}
