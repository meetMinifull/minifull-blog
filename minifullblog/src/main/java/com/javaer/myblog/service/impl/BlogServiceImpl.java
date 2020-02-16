package com.javaer.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.entity.BlogTagRelation;
import com.javaer.myblog.entity.Category;
import com.javaer.myblog.entity.Tag;
import com.javaer.myblog.mapper.BlogMapper;
import com.javaer.myblog.mapper.BlogTagMapper;
import com.javaer.myblog.mapper.CategoryMapper;
import com.javaer.myblog.mapper.TagMapper;
import com.javaer.myblog.service.BlogService;
import com.javaer.myblog.util.MarkDownUtil;
import com.javaer.myblog.util.PageResult;
import com.javaer.myblog.vo.BlogDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogTagMapper blogTagRelationMapper;

    @Override
    public PageResult getAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.findBlogList();
        int totalBlogs = blogMapper.getTotalBlogs();
        PageResult pageResult = new PageResult(blogs, totalBlogs, 10, pageNum);
        return pageResult;
    }

    @Override
    public PageResult getAllByKeyWord(int pageNum, int pageSize, String keyWord) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.getBlogByKeyWord(keyWord);
        int totalBlogs = blogMapper.getTotalBlogs();
        PageResult pageResult = new PageResult(blogs, totalBlogs, 10, pageNum);
        return pageResult;
    }

    @Override
    public Blog getBlogById(Long blogId) {
        return blogMapper.selectByPrimaryKey(blogId);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogMapper.deleteBatch(ids) > 0;
    }

    @Override
    public String updateBlog(Blog blog) {
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if (blogForUpdate == null) {
            return "数据不存在";
        }
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
        Category blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null) {
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        } else {
            //设置博客分类名称
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "标签数量限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<Tag> tagListForInsert = new ArrayList<>();
        //所有的tag对象，用于建立关系数据
        List<Tag> allTagsList = new ArrayList<>();
        for (int i = 0; i < tags.length; i++) {
            Tag tag = tagMapper.selectByTagName(tags[i]);
            if (tag == null) {
                //不存在就新增
                Tag tempTag = new Tag();
                tempTag.setTagName(tags[i]);
                tagListForInsert.add(tempTag);
            } else {
                allTagsList.add(tag);
            }
        }
        //新增标签数据不为空->新增标签数据
        if (!CollectionUtils.isEmpty(tagListForInsert)) {
            tagMapper.batchInsertBlogTag(tagListForInsert);
        }
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        //新增关系数据
        allTagsList.addAll(tagListForInsert);
        for (Tag tag : allTagsList) {
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            blogTagRelation.setTagId(tag.getTagId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息->修改分类排序值->删除原关系数据->保存新的关系数据
        categoryMapper.updateByPrimaryKeySelective(blogCategory);
        blogTagRelationMapper.deleteByBlogId(blog.getBlogId());
        blogTagRelationMapper.batchInsert(blogTagRelations);
        if (blogMapper.updateByPrimaryKeySelective(blogForUpdate) > 0) {
            return "success";
        }
        return "修改失败";
    }

    @Override
    public List<Blog> findTopFavBlog() {
        return blogMapper.findTopFavBlog();
    }

    @Override
    public List<Blog> findLatestBlog() {
        return blogMapper.findLatestBlog();
    }

    @Override
    public PageResult getBlogByCategoryName(String categoryName, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> blogs = blogMapper.getBlogByCategoryName(categoryName);
        if(blogs!= null){
            int totalBlogs = blogMapper.getTotalBlogsByCategoryName(categoryName);
            PageResult pageResult = new PageResult(blogs, totalBlogs, pageSize, pageNum);
            return pageResult;
        }
        return null;
    }


    @Override
    public String saveBlog(Blog blog) {
        Category category = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if(category == null){
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        }else {
            blog.setBlogCategoryName(category.getCategoryName());
            category.setCategoryRank(category.getCategoryRank()+1);

        }
        //处理标签 有就添加 没有就新增
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length>6){
            return "标签数量限制为6";
        }
        if (blogMapper.insertSelective(blog) > 0) {
            //新增的tag对象
            List<Tag> tagListForInsert = new ArrayList<>();
            //所有的tag对象，用于建立关系数据
            List<Tag> allTagsList = new ArrayList<>();
            for (int i = 0; i < tags.length; i++) {
                Tag tag = tagMapper.selectByTagName(tags[i]);
                if (tag == null) {
                    //不存在就新增
                    Tag tempTag = new Tag();
                    tempTag.setTagName(tags[i]);
                    tagListForInsert.add(tempTag);
                } else {
                    allTagsList.add(tag);
                }
            }
            //新增标签数据并修改分类排序值
            if (!CollectionUtils.isEmpty(tagListForInsert)) {
                tagMapper.batchInsertBlogTag(tagListForInsert);
            }
            categoryMapper.updateByPrimaryKeySelective(category);
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            //新增关系数据
            allTagsList.addAll(tagListForInsert);
            for (Tag tag : allTagsList) {
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if (blogTagRelationMapper.batchInsert(blogTagRelations) > 0) {
                return "success";
            }
        }
        return "保存失败";
    }

    @Override
    public BlogDetailVO getBlogDetail(Long id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        //不为空且状态为已发布
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null) {
            return blogDetailVO;
        }
        return null;
    }

    @Override
    public List<Blog> getBlogByKeyWord(String keyword) {
        return blogMapper.getBlogByKeyWord(keyword);

    }

    @Override
    public int getTotalBlogs() {
        return blogMapper.getTotalBlogs();
    }

    /**
     * 方法抽取
     *
     * @param blog
     * @return
     */
    private BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus() == 1) {
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateByPrimaryKey(blog);
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, blogDetailVO);
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            Category blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            if (blogCategory == null) {
                blogCategory = new Category();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            //分类信息
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            if (!StringUtils.isEmpty(blog.getBlogTags())) {
                //标签设置
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            //设置评论数

            return blogDetailVO;
        }
        return null;
    }
}
