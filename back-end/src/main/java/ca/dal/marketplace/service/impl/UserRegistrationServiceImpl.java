package ca.dal.marketplace.service.impl;

import ca.dal.marketplace.dao.UserRepository;
import ca.dal.marketplace.entity.User;
import ca.dal.marketplace.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setEnabled(false);
        user.setFailedAttempts(0);
        user.setLocked(false);
        userRepository.save(user);
        return user;
    }
}