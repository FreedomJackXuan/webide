package com.webide.mapper;

import com.webide.bean.User;
import com.webide.bean.UserDocker;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDockerMapper {
    @Select("select * from t_userdocker where id = #{id}")
    public UserDocker getById(@Param("id")long id);

    @Insert("insert into t_userdocker (`id`, `dockerid`, `dockerfile`) values (#{id}, #{dockerid}, #{dockerfile})")
    public void insertUser(UserDocker toBeInsert);

    @Update("update t_userdocker set dockerid = #{dockerid}, dockerfile = #{dockerfile} where id = #{id}")
    public void update(UserDocker toBeUpdate);
}
