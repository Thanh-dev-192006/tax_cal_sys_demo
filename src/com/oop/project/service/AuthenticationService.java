package com.oop.project.service;

import com.oop.project.exception.InvalidCredentialException;
import com.oop.project.model.User;
import com.oop.project.repository.UserRepository;

public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = new UserRepository();
    }

    public User authenticate(String username, String password) throws InvalidCredentialException {
        User user = userRepository.findUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidCredentialException("Invalid username or password");
        }
        return user;
    }

    public void logLogin(String username) {
        // Log to a file or console
        System.out.println("User " + username + " logged in at " + java.time.LocalDateTime.now());
    }

    public void logLogout(String username) {
        System.out.println("User " + username + " logged out at " + java.time.LocalDateTime.now());
    }
}