package com.javaer.myblog.mapper;

import com.javaer.myblog.entity.BlogTagRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogTagMapper {
    List<Long> selectDistinctTagIds(Integer[] tagIds);

    int batchInsert(@Param("relationList") List<BlogTagRelation> blogTagRelationList);

    int deleteByBlogId(Long blogId);



}
