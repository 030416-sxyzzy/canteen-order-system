package com.canteen.controller;

import com.canteen.common.Result;
import com.canteen.dto.UserDTO;
import com.canteen.dto.LoginDTO;
import com.canteen.service.UserService;
import com.canteen.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserDTO userDTO) {
        Long userId = userService.register(userDTO);
        return Result.success(userId);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody LoginDTO loginDTO) {
        UserVO userVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword(), loginDTO.getUserType());
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.update(id, userDTO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO userVO = userService.getById(id);
        return Result.success(userVO);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/list")
    public Result<List<UserVO>> list() {
        List<UserVO> userVOList = userService.list();
        return Result.success(userVOList);
    }
} 