package com.kitchen.backend_springbook_kichen.service.impl;

import com.kitchen.backend_springbook_kichen.entity.User;
import com.kitchen.backend_springbook_kichen.mapper.UserMapper;
import com.kitchen.backend_springbook_kichen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 邮箱正则
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // 手机号正则（中国）
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 验证用户数据合法性
     */
    private void validateUser(User user, boolean isNew) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is empty");
        }
        if (user.getUsername().length() < 3) {
            throw new RuntimeException("Username too short");
        }
        if (user.getUsername().length() > 50) {
            throw new RuntimeException("Username too long");
        }

        if (isNew) {
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new RuntimeException("Password is empty");
            }
            if (user.getPassword().length() < 6) {
                throw new RuntimeException("Password too short");
            }
        }

        // 验证邮箱格式
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
                throw new RuntimeException("Invalid email");
            }
        }

        // 验证手机号格式
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (!PHONE_PATTERN.matcher(user.getPhone()).matches()) {
                throw new RuntimeException("Invalid phone number");
            }
        }

        // 检查用户名是否已存在（新增时）
        if (isNew) {
            User existing = userMapper.findByUsername(user.getUsername());
            if (existing != null) {
                throw new RuntimeException("Username already exists");
            }
        }
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            throw new RuntimeException("Username or password is null");
        }
        User user = userMapper.findByUsernameAndPassword(username, password);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("User is not active");
        }
        return user;
    }

    @Override
    @Transactional
    public User register(User user) {
        validateUser(user, true);
        user.setRole("user");
        user.setStatus(1);
        int result = userMapper.insert(user);
        if (result > 0) {
            return userMapper.findById(user.getId());
        }
        throw new RuntimeException("User not found");
    }

    @Override
    public User getUserById(Integer id) {
        if (id == null) {
            throw new RuntimeException("Id is null");
        }
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        if (username == null) {
            throw new RuntimeException("Username is null");
        }
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userMapper.search(keyword);
    }

    @Override
    @Transactional
    public User addUser(User user) {
        validateUser(user, true);

        // 默认密码为123456
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("123456");
        }

        if (user.getRole() == null) {
            user.setRole("user");
        }
        user.setStatus(1);

        int result = userMapper.insert(user);
        if (result > 0) {
            return userMapper.findById(user.getId());
        }
        throw new RuntimeException("User not found");
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new RuntimeException("Userid is null");
        }

        User existing = userMapper.findById(user.getId());
        if (existing == null) {
            throw new RuntimeException("User not found");
        }

        // 更新允许修改的字段
        existing.setRealName(user.getRealName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());

        int result = userMapper.update(existing);
        if (result > 0) {
            return userMapper.findById(user.getId());
        }
        throw new RuntimeException("User not found");
    }

    @Override
    @Transactional
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        if (userId == null) {
            throw new RuntimeException("Userid is null");
        }
        if (oldPassword == null || newPassword == null) {
            throw new RuntimeException("Old password is null");
        }
        if (newPassword.length() < 6) {
            throw new RuntimeException("New password too short");
        }

        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password does not match");
        }

        int result = userMapper.updatePassword(userId, newPassword);
        return result > 0;
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        if (id == null) {
            throw new RuntimeException("Userid is null");
        }

        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // 不能删除管理员账号（可选）
        if ("admin".equals(user.getRole())) {
            throw new RuntimeException("Admin can not be deleted");
        }

        int result = userMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public long getUserCount() {
        return userMapper.count();
    }

    @Override
    public List<Map<String, Object>> getUserStats() {
        return userMapper.getStatsByRole();
    }
}