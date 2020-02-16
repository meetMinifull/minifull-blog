package com.javaer.myblog.web.blog;

import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.service.BlogService;
import com.javaer.myblog.service.CategoryService;
import com.javaer.myblog.service.TagService;
import com.javaer.myblog.util.PageResult;
import com.javaer.myblog.vo.BlogDetailVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MyBlogController {
    @Resource
    private BlogService blogService;
    @Resource
    private TagService tagService;
    @Resource
    private CategoryService categoryService;
    @GetMapping({"/", "/index", "index.html"})
    public String index(HttpServletRequest request) {
        return this.page(request, 1);
    }

    /**
     * 首页 分页数据
     *
     * @return
     */
    @GetMapping({"/page/{pageNum}"})
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        if(pageNum <= 0) {
            pageNum = 1;
        }
        PageResult blogPageResult = blogService.getAll(pageNum, 10);
        if (blogPageResult == null) {
            return "error/404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.findLatestBlog());
        request.setAttribute("hotBlogs", blogService.findTopFavBlog());
        request.setAttribute("allTags", tagService.getAll());
        request.setAttribute("allCategories", categoryService.getCategoryListInNum(2));
        return "blog/index";
    }

    //获取所有类
    @GetMapping({"/allcategory"})
    public String allcategory(HttpServletRequest request){
        return allcategory(request, 1);
    }

    @GetMapping({"/allcategory/{pageNum}"})
    public String allcategory(HttpServletRequest request, @PathVariable("pageNum") Integer pageNum){
        PageResult blogPageResult = categoryService.getAll(pageNum, 9);
        if (blogPageResult == null) {
            return "error/404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        return "blog/category";
    }

    //获取相应类别的文章
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName){
        return category(request, categoryName, 1);
    }

    @GetMapping({"/category/{categoryName}/{pageNum}"})
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("pageNum") Integer pageNum){
        PageResult blogPageResult = blogService.getBlogByCategoryName(categoryName,pageNum, 9);
        if (blogPageResult == null) {
            return "error/404";
        }
        request.setAttribute("categoryName",categoryName);
        request.setAttribute("blogPageResult", blogPageResult);
        return "blog/archive";
    }

    @GetMapping({"/blog/{blogId}", "/article/{blogId}"})
    public String detail(HttpServletRequest request, @PathVariable("blogId") Long blogId, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            PageResult blogPageResult = blogService.getBlogByCategoryName(blogDetailVO.getBlogCategoryName(),1, 5);
            request.setAttribute("blogPageResult", blogPageResult);
        }
        request.setAttribute("pageName", "详情");
        request.setAttribute("newBlogs", blogService.findLatestBlog());
        request.setAttribute("hotBlogs", blogService.findTopFavBlog());

        return "blog/single";
    }

    @GetMapping({"/blog/aboutme", "/article/aboutme"})
    public String aboutme(HttpServletRequest request, @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage) {
        List<Blog> lists = blogService.getBlogByKeyWord("ABOUT ME");
        if(lists.size()>0){
            Blog aboutme = lists.get(0);
            BlogDetailVO blogDetailVO = blogService.getBlogDetail(aboutme.getBlogId());
            if (blogDetailVO != null) {
                request.setAttribute("blogDetailVO", blogDetailVO);
                PageResult blogPageResult = blogService.getBlogByCategoryName(blogDetailVO.getBlogCategoryName(),1, 5);
                request.setAttribute("blogPageResult", blogPageResult);
            }
            request.setAttribute("pageName", "详情");
            request.setAttribute("newBlogs", blogService.findLatestBlog());
            request.setAttribute("hotBlogs", blogService.findTopFavBlog());

            return "blog/single";
        }
       return "error/404";

    }

    @GetMapping("/search")
    public String getBlogByKeyWord(HttpServletRequest request, String keyWord){
        List<Blog> list = blogService.getBlogByKeyWord(keyWord);
        if(list == null){
            return "error/404.html";
        }
        PageResult pageResult = new PageResult(list, list.size(), list.size(), 1);
        request.setAttribute("blogPageResult",pageResult);
        return "blog/archive";
    }
}
