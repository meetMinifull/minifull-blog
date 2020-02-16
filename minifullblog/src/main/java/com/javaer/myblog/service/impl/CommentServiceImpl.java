package com.javaer.myblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.javaer.myblog.entity.Blog;
import com.javaer.myblog.entity.BlogComment;
import com.javaer.myblog.mapper.BlogCommentMapper;
import com.javaer.myblog.service.CommentService;
import com.javaer.myblog.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private BlogCommentMapper blogCommentMapper;

    //存放迭代找出的所有子代的集合
    private List<BlogComment> tempReplys = new ArrayList<>();

    @Override
    public List<BlogComment> listCommentByBlogId(Long blogId) {
        //先找所以最上级的评论
        List<BlogComment> comments = blogCommentMapper.findByBlogIdAndParentCommentNull(blogId);

        return eachComment(comments);
    }

    @Override
    public void saveComment(BlogComment blogComment) {
        Long parentCommentId = blogComment.getParentcommentId();
        if (parentCommentId != -1) {
            blogComment.setParentComment(blogCommentMapper.getCommentByCommentId(parentCommentId));
        } else {
            blogComment.setParentComment(null);
        }
        blogComment.setCommentCreateTime(new Date());
        blogCommentMapper.save(blogComment);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<BlogComment> eachComment(List<BlogComment> comments) {
        List<BlogComment> commentsView = new ArrayList<>();
        for (BlogComment comment : comments) {
            BlogComment c = new BlogComment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<BlogComment> comments) {
        for (BlogComment comment : comments) {
            List<BlogComment> replys1 = blogCommentMapper.findReplyCommentsByParentCommentId(comment.getCommentId());
            for (BlogComment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                reply1.setParentComment(comment);
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);

            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

        /**
         * 递归迭代，剥洋葱
         * @param comment 被迭代的对象
         * @return
         */
        private void recursively(BlogComment comment) {
            tempReplys.add(comment);//顶节点添加到临时存放集合
            if (blogCommentMapper.findReplyCommentsByParentCommentId(comment.getCommentId()).size()>0) {
                List<BlogComment> replys = blogCommentMapper.findReplyCommentsByParentCommentId(comment.getCommentId());
                for (BlogComment reply : replys) {
                    reply.setParentComment(comment);
                    tempReplys.add(reply);
                    if (blogCommentMapper.findReplyCommentsByParentCommentId(reply.getCommentId()).size()>0) {
                        recursively(reply);
                    }
                }
            }
        }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogCommentMapper.deleteBatch(ids) > 0;
    }

    @Override
    public PageResult getAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BlogComment> blogCategories = blogCommentMapper.getAllComment();
        int totalCategories = blogCommentMapper.getTotalComments();
        PageResult pageResult = new PageResult(blogCategories, totalCategories, 10, pageNum);
        return pageResult;
    }

    @Override
    public int getTotalComments() {
        return blogCommentMapper.getTotalComments();
    }

}
