package com.journal.TradingApi.service;

import com.journal.TradingApi.exception.custom.UserNotFoundException;
import com.journal.TradingApi.exception.custom.UserValidationException;
import com.journal.TradingApi.model.User;
import com.journal.TradingApi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    // Create a new user
    public User createUser(User user) {

        if (user.getUsername() == null || user.getUsername().isBlank()||
        user.getEmail() == null || user.getEmail().isBlank()||
        user.getPassword() == null || user.getPassword().isBlank()) {

            throw new UserValidationException("Username, email, and password are required");
        }

        return userRepo.save(user);
    }

    // Retrieve all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Retrieve a user by ID, throws exception if not found
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    // Delete a user and all associated trades (cascade delete)
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepo.delete(user);
    }
}