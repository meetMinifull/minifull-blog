package com.javaer.myblog.service;

import com.javaer.myblog.entity.BlogComment;
import com.javaer.myblog.util.PageResult;

import java.util.List;

public interface CommentService {
    List<BlogComment> listCommentByBlogId(Long blogId);

    void saveComment(BlogComment blogComment);

    Boolean deleteBatch(Integer[] ids);

    //查询 可以配合pageHelper进行分页
    PageResult getAll(int pageNum, int pageSize);

    int getTotalComments();
}
