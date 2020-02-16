package com.javaer.myblog.mapper;

import com.javaer.myblog.entity.Tag;

import java.util.List;

public interface TagMapper {
    List<Tag> getAll();

    //总条数
    int getTotalTags();

    Tag selectByTagName(String tagName);

    int insertSelective(Tag record);

    int deleteBatch(Integer[] ids);

    int batchInsertBlogTag(List<Tag> tagList);
}
