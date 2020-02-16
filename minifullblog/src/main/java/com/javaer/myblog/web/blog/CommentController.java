package com.javaer.myblog.web.blog;

import com.javaer.myblog.entity.BlogComment;
import com.javaer.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<BlogComment> comments = commentService.listCommentByBlogId(blogId);
        int totalComment = 0;
        for (BlogComment comment : comments) {
            totalComment++;
            List<BlogComment> replyComments = comment.getReplyComments();
            for (BlogComment replyComment : replyComments) {
                totalComment++;
            }
        }
        model.addAttribute("totalComment", totalComment);
        model.addAttribute("comments", comments);

        return "blog/single :: commentList";
    }

    @RequestMapping("/comments")
    public String save(BlogComment comment) {
        System.out.println("coming in");
        Long blogId = comment.getBlogId();
        System.out.println(comment);
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }

}
