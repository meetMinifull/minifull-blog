package com.javaer.myblog.mapper;


import com.javaer.myblog.entity.Category;
import com.javaer.myblog.util.PageQueryUtil;

import java.util.List;

public interface CategoryMapper {

    List<Category> getAll();

    List<Category> getCategoryListInNum(int num);

    //删除分类 这里的删除是软删除，把分类的is_deleted字段设为1
    int deleteByPrimaryKey(Integer categoryId);

    //批量删除 其实用这个就能实现删除
    int deleteBatch(Integer[] ids);

    //根据名称查询分类
    Category selectByCategoryName(String categoryName);

    //新增分类
    int insertSelective(Category record);

    //通过主键查询
    Category selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(Category record);

    //总条数
    int getTotalCategories();
}