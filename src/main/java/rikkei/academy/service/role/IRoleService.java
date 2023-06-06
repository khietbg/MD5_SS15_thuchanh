package rikkei.academy.service.role;

import rikkei.academy.model.Role;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(String name);
}
