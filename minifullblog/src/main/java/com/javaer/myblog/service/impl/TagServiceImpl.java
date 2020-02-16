package com.javaer.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaer.myblog.entity.Tag;
import com.javaer.myblog.mapper.BlogTagMapper;
import com.javaer.myblog.mapper.TagMapper;
import com.javaer.myblog.service.TagService;
import com.javaer.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogTagMapper relationMapper;

    @Override
    public PageResult getAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Tag> blogTags = tagMapper.getAll();
        int totalCategories = tagMapper.getTotalTags();
        PageResult pageResult = new PageResult(blogTags, totalCategories, 10, pageNum);
        return pageResult;
    }

    public List<Tag> getAll(){
        return tagMapper.getAll();
    }

    @Override
    public int getTotalTags() {
        return tagMapper.getTotalTags();
    }

    @Override
    public Boolean saveTag(String tagName) {
        Tag temp = tagMapper.selectByTagName(tagName);
        if (temp == null) {
            Tag blogTag = new Tag();
            blogTag.setTagName(tagName);
            return tagMapper.insertSelective(blogTag) > 0;
        }
        return false;
    }
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        //已存在关联关系不删除
        List<Long> relations = relationMapper.selectDistinctTagIds(ids);
        if (!CollectionUtils.isEmpty(relations)) {
            return false;
        }
        //删除tag
        return tagMapper.deleteBatch(ids) > 0;
    }
}
