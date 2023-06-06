package rikkei.academy.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rikkei.academy.model.User;
import rikkei.academy.repository.IUserRepository;

import java.util.Optional;
@Service

public class UserServiceIpm implements IUserService{
    @Autowired
    private IUserRepository userRepository;
    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByUserName(name);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
