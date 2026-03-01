package com.CivicSystem.Service;

import com.CivicSystem.Model.User;
import com.CivicSystem.Repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws Exception {
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    public User findByFirebaseUid(String firebaseUid) throws Exception {
        User user = userRepository.findByFirebaseUid(firebaseUid);

        if (user == null) {
            throw new RuntimeException("User profile not found");
        }

        return user;
    }
}
