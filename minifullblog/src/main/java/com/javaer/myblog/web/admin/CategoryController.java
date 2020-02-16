package com.javaer.myblog.web.admin;

import com.github.pagehelper.PageHelper;
import com.javaer.myblog.entity.Category;
import com.javaer.myblog.service.CategoryService;
import com.javaer.myblog.util.PageResult;
import com.javaer.myblog.util.Result;
import com.javaer.myblog.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    //分页查询
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request) {
        request.setAttribute("path", "categories");
        return "admin/category";
    }

    ///admin/categories/list
    @GetMapping(value = "/categories/list")
    @ResponseBody
    private Result List(@RequestParam Map<String, Object> params){
        System.out.println(params.get("page"));
        System.out.println(params.get("limit"));
        int pageNum = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        PageResult pageResult = categoryService.getAll(pageNum,pageSize);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    //删除
    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (categoryService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    /**
     * 分类添加
     */
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestParam("categoryName") String categoryName,
                       @RequestParam("categoryIcon") String categoryIcon){
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        if (categoryService.saveCategory(categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

    /**
     * 分类修改
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("categoryId") Integer categoryId,
                         @RequestParam("categoryName") String categoryName,
                         @RequestParam("categoryIcon") String categoryIcon) {
        System.out.println(categoryId);
        if (StringUtils.isEmpty(categoryName)) {
            return ResultGenerator.genFailResult("请输入分类名称！");
        }
        if (StringUtils.isEmpty(categoryIcon)) {
            return ResultGenerator.genFailResult("请选择分类图标！");
        }
        if (categoryService.updateCategory(categoryId, categoryName, categoryIcon)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("分类名称重复");
        }
    }

}
