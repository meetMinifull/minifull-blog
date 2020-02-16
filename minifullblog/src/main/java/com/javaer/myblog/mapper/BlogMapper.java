package com.javaer.myblog.mapper;

import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.util.PageQueryUtil;

import java.util.List;

public interface BlogMapper {
    int insertSelective(Blog record);

    List<Blog> findBlogList();

    int getTotalBlogs();

    int getTotalBlogsByCategoryName(String categoryName);

    Blog selectByPrimaryKey(Long blogId);

    int deleteBatch(Integer[] ids);

    int updateByPrimaryKeySelective(Blog record);

    List<Blog> findTopFavBlog();

    List<Blog> findLatestBlog();

    List<Blog> getBlogByCategoryName(String categoryName);

    int updateByPrimaryKey(Blog record);

    List<Blog> getBlogByKeyWord(String keyWord);
}
