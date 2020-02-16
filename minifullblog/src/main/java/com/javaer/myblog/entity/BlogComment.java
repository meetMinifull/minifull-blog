package com.javaer.myblog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class BlogComment {
    private Long commentId;

    private Long blogId;

    private String commentator;

    private String email;

    private String commentBody;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commentCreateTime;

    private Byte commentStatus;

    private Byte isDeleted;

    private Byte isAdmin;

    private BlogComment parentComment;

    private Blog blog;

    private Long parentcommentId;

    private List<BlogComment> replyComments;

    public List<BlogComment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<BlogComment> replyComments) {
        this.replyComments = replyComments;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getParentcommentId() {
        return parentcommentId;
    }

    public void setParentcommentId(Long parentcommentId) {
        this.parentcommentId = parentcommentId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Date getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(Date commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    public Byte getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Byte commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    public BlogComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(BlogComment parentComment) {
        this.parentComment = parentComment;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "commentId=" + commentId +
                ", blogId=" + blogId +
                ", commentator='" + commentator + '\'' +
                ", email='" + email + '\'' +
                ", commentBody='" + commentBody + '\'' +
                ", commentCreateTime=" + commentCreateTime +
                ", commentStatus=" + commentStatus +
                ", isDeleted=" + isDeleted +
                ", isAdmin=" + isAdmin +
                ", parentComment=" + parentComment +
                ", blog=" + blog +
                ", parentcommentId=" + parentcommentId +
                ", replyComments=" + replyComments +
                '}';
    }
}
