package com.canteen.service.impl;

import com.canteen.dto.UserDTO;
import com.canteen.entity.User;
import com.canteen.mapper.UserMapper;
import com.canteen.service.UserService;
import com.canteen.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Long register(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 密码格式校验（6-20位数字和字母组合）
        if (!isValidPassword(userDTO.getPassword())) {
            throw new RuntimeException("密码格式不正确，请输入6-20位数字和字母组合");
        }

        // 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 直接存储密码（简化处理，便于学习）
        user.setPassword(userDTO.getPassword());
        
        // 保存用户
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    public UserVO login(String username, String password, Integer userType) {
        // 根据用户名查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户名不存在：" + username);
        }

        // 直接比较密码
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误，请输入正确的密码");
        }

        // 验证用户类型
        if (!user.getUserType().equals(userType)) {
            throw new RuntimeException("用户角色不匹配，注册时选择的是：" + 
                (user.getUserType() == 1 ? "学生" : "管理员") + 
                "，登录时选择的是：" + (userType == 1 ? "学生" : "管理员"));
        }

        // 转换为VO对象
        return convertToVO(user);
    }

    @Override
    public void update(Long id, UserDTO userDTO) {
        // 检查用户是否存在
        User existUser = userMapper.selectById(id);
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 如果密码不为空，则校验密码格式
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            if (!isValidPassword(userDTO.getPassword())) {
                throw new RuntimeException("密码格式不正确，请输入6-20位数字和字母组合");
            }
        }

        // 更新用户信息
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        
        // 如果密码不为空，则直接存储
        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        
        userMapper.update(user);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public UserVO getById(Long id) {
        User user = userMapper.selectById(id);
        return user != null ? convertToVO(user) : null;
    }

    @Override
    public List<UserVO> list() {
        List<User> users = userMapper.selectAll();
        return users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 密码格式校验
     * 要求：6-20位字符
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 20) {
            return false;
        }
        return true;
    }

    /**
     * 将User对象转换为UserVO对象
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 设置用户类型描述
        userVO.setUserTypeDesc(user.getUserType() == 1 ? "学生" : "管理员");
        return userVO;
    }
} 