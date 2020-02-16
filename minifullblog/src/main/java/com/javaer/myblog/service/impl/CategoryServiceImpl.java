package com.javaer.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.entity.Category;
import com.javaer.myblog.mapper.CategoryMapper;
import com.javaer.myblog.service.CategoryService;
import com.javaer.myblog.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public PageResult getAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> blogCategories = categoryMapper.getAll();
        int totalCategories = categoryMapper.getTotalCategories();
        PageResult pageResult = new PageResult(blogCategories, totalCategories, 10, pageNum);
        return pageResult;
    }

    @Override
    public List<Category> getAll() {
        return categoryMapper.getAll();
    }

    @Override
    public List<Category> getCategoryListInNum(int num) {
        int totalNum = categoryMapper.getTotalCategories();
        if(num > totalNum){
            return categoryMapper.getCategoryListInNum(totalNum);
        }

        return categoryMapper.getCategoryListInNum(num);
    }


    @Override
    public Boolean saveCategory(String categoryName, String categoryIcon) {
        Category temp = categoryMapper.selectByCategoryName(categoryName);
        if (temp == null) {
            Category blogCategory = new Category();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return categoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }

    @Override
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            category.setCategoryName(categoryName);
            category.setCategoryIcon(categoryIcon);
            return categoryMapper.updateByPrimaryKeySelective(category) > 0;
        }
        return false;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        System.out.println(ids[0]);
        if (ids.length < 1) {
            return false;
        }
        return categoryMapper.deleteBatch(ids) > 0;
    }

    @Override
    public int getTotalCategories() {
       return categoryMapper.getTotalCategories();
    }
}
