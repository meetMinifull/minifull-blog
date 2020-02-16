package com.javaer.myblog.web.admin;

import com.javaer.myblog.service.CommentService;
import com.javaer.myblog.util.PageResult;
import com.javaer.myblog.util.Result;
import com.javaer.myblog.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminCommentController {
    @Autowired
    CommentService commentService;
    @GetMapping("/comments")
    public String list(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("刪除失败");
        }
    }

    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.parseInt(params.get("page").toString());
        int pageSize = Integer.parseInt(params.get("limit").toString());

        PageResult pageResult = commentService.getAll(pageNum,pageSize);
        return ResultGenerator.genSuccessResult(pageResult);
    }
}
