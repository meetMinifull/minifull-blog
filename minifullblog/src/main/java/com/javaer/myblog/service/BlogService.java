package com.javaer.myblog.service;

import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.util.PageResult;
import com.javaer.myblog.vo.BlogDetailVO;

import java.util.List;

public interface BlogService {
    String saveBlog(Blog blog);

    PageResult getAll(int pageNum, int pageSize);

    PageResult getAllByKeyWord(int pageNum, int pageSize, String keyWord);

    /**
     * 根据id获取详情
     *
     * @param blogId
     * @return
     */
    Blog getBlogById(Long blogId);

    Boolean deleteBatch(Integer[] ids);

    String updateBlog(Blog blog);

    List<Blog> findTopFavBlog();

    List<Blog> findLatestBlog();

    PageResult getBlogByCategoryName(String categoryName, int pageNum, int pageSize);

    BlogDetailVO getBlogDetail(Long id);

    List<Blog> getBlogByKeyWord(String keyword);

    int getTotalBlogs();

}
