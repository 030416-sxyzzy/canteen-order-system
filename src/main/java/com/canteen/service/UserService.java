package com.canteen.service;

import com.canteen.dto.UserDTO;
import com.canteen.vo.UserVO;
import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户注册
     * @param userDTO 用户信息
     * @return 用户ID
     */
    Long register(UserDTO userDTO);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param userType 用户类型
     * @return 用户信息
     */
    UserVO login(String username, String password, Integer userType);

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param userDTO 用户信息
     */
    void update(Long id, UserDTO userDTO);

    /**
     * 删除用户
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getById(Long id);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<UserVO> list();
} 