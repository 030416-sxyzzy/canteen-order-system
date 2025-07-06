package com.canteen.mapper;

import com.canteen.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    /**
     * 新增用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(Long id);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(String username);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> selectAll();
} 