package com.javaer.myblog.service;

import com.javaer.myblog.entity.Category;
import com.javaer.myblog.util.PageResult;

import java.util.List;

public interface CategoryService {
    //查询 可以配合pageHelper进行分页
    PageResult getAll(int pageNum, int pageSize);

    List<Category> getAll();

    List<Category> getCategoryListInNum(int num);

    //先判断是否已经存在该分类再插入
    Boolean saveCategory(String categoryName,String categoryIcon);

    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);

    Boolean deleteBatch(Integer[] ids);

    int getTotalCategories();
}
