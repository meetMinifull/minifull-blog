package com.javaer.myblog.mapper;

import com.javaer.myblog.entity.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserMapper {
    AdminUser getUser(@Param("userName") String userName, @Param("password") String password);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);
}
