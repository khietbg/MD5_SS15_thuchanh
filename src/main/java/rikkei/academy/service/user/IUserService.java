package rikkei.academy.service.user;

import rikkei.academy.model.User;

import java.util.Optional;

public interface IUserService {
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByName(String name);
    User save(User user);
}
