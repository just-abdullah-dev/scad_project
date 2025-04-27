package com.sprintpredictor.service;

import com.sprintpredictor.entity.User;
import com.sprintpredictor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User updateUser(Long userId, String newName, String newPassword, String newGithubUsername) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        User user = optionalUser.get();

        if (newName != null && !newName.isEmpty()) {
            user.setName(newName);
        }

        if (newGithubUsername != null && !newGithubUsername.isEmpty()) {
            user.setGithubUsername(newGithubUsername);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        return userRepository.save(user);
    }
}
