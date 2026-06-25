package com.kitchen.backend_springbook_kichen.service;

import com.kitchen.backend_springbook_kichen.entity.User;
import java.util.List;
import java.util.Map;

/**
 * User Service Interface
 */
public interface UserService {

    /**
     * User login
     * @param username Username
     * @param password Password
     * @return User information
     */
    User login(String username, String password);

    /**
     * User registration
     * @param user User information
     * @return Registered user
     */
    User register(User user);

    /**
     * Get user by ID
     * @param id User ID
     * @return User information
     */
    User getUserById(Integer id);

    /**
     * Get user by username
     * @param username Username
     * @return User information
     */
    User getUserByUsername(String username);

    /**
     * Get all users
     * @return User list
     */
    List<User> getAllUsers();

    /**
     * Search users
     * @param keyword Search keyword
     * @return User list
     */
    List<User> searchUsers(String keyword);

    /**
     * Add a new user
     * @param user User information
     * @return Added user
     */
    User addUser(User user);

    /**
     * Update user information
     * @param user User information
     * @return Updated user
     */
    User updateUser(User user);

    /**
     * Change password
     * @param userId User ID
     * @param oldPassword Old password
     * @param newPassword New password
     * @return Operation result
     */
    boolean changePassword(Integer userId, String oldPassword, String newPassword);

    /**
     * Delete user
     * @param id User ID
     * @return Operation result
     */
    boolean deleteUser(Integer id);

    /**
     * Get total number of users
     * @return Total count
     */
    long getUserCount();

    /**
     * Get user statistics grouped by role
     * @return Statistical data
     */
    List<Map<String, Object>> getUserStats();
}