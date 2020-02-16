package com.javaer.myblog.service;

import com.javaer.myblog.entity.AdminUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


public interface AdminService {
    AdminUser login(String userName, String password);

    /**
     * 获取用户信息
     *
     * @param loginUserId
     * @return
     */
    AdminUser getUserDetailById(Integer loginUserId);

    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

    /**
     * 修改当前登录用户的名称信息
     *
     * @param loginUserId
     * @param loginUserName
     * @param nickName
     * @return
     */
    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

}
