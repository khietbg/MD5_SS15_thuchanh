package rikkei.academy.service.role;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rikkei.academy.model.Role;
import rikkei.academy.repository.IRoleRepository;

import java.util.Optional;
@Service
public class RoleServiceIpm implements IRoleService{
    @Autowired
    private IRoleRepository roleRepository;
    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }
}
