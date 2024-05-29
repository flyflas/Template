package icu.xiaobai.librarydemo.mapper;

import icu.xiaobai.librarydemo.entity.User;

/**
* @author xiaobai
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-15 14:52:42
* @Entity icu.xiaobai.librarydemo.entity.User
*/
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByEmail(String email);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}
