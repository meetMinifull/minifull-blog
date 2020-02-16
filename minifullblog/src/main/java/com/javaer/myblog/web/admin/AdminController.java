package com.javaer.myblog.web.admin;

import com.javaer.myblog.entity.AdminUser;
import com.javaer.myblog.mapper.CategoryMapper;
import com.javaer.myblog.mapper.TagMapper;
import com.javaer.myblog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CommentService commentService;

    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @GetMapping({ "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        request.setAttribute("categoryCount", categoryService.getTotalCategories());
        request.setAttribute("blogCount", blogService.getTotalBlogs());
        request.setAttribute("tagCount", tagService.getTotalTags());
        request.setAttribute("commentCount",commentService.getTotalComments());
        request.setAttribute("path", "index");
        return "admin/index";
    }

    @PostMapping(value = "/login")
    public String login(String userName,
                        String password,
                        HttpSession session) {
        System.out.println(userName);
        System.out.println(password);
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        AdminUser adminUser = adminService.login(userName, password);
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为 即四小时
            session.setMaxInactiveInterval(60 * 60 * 4);
            return "redirect:/admin/index";
        } else {
            System.out.println("登陆失败");
            session.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (StringUtils.isEmpty(originalPassword) || StringUtils.isEmpty(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (StringUtils.isEmpty(loginUserName) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }


}
