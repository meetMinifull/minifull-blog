package com.javaer.myblog.service;

import com.javaer.myblog.entity.Tag;
import com.javaer.myblog.util.PageResult;

import java.util.List;

public interface TagService {
    //查询 可以配合pageHelper进行分页
    PageResult getAll(int pageNum, int pageSize);

    Boolean saveTag(String tagName);

    public Boolean deleteBatch(Integer[] ids);

    List<Tag> getAll();

    int getTotalTags();
}
