package com.webide.mapper;

import com.webide.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from t_role where id = #{id}")
    public User getById(@Param("id")long id);

    @Update("update t_role set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);

    @Insert("insert into t_role (`id`, `nickname`, `password`, `register_date`, `last_login_date`, `salt`) values (#{id}, #{nickname}, #{password}, #{registerDate}, #{lastLoginDate}, #{salt})")
    public void insertUser(User toBeInsert);
}
