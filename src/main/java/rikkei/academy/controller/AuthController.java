package rikkei.academy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rikkei.academy.dto.request.SignInForm;
import rikkei.academy.dto.request.SignUpForm;
import rikkei.academy.dto.response.JwtResponse;
import rikkei.academy.dto.response.ResponseMessage;
import rikkei.academy.model.Role;
import rikkei.academy.model.RoleName;
import rikkei.academy.model.User;
import rikkei.academy.security.jwt.JwtProvider;
import rikkei.academy.security.userPrincipal.UserPrinciple;
import rikkei.academy.service.role.IRoleService;
import rikkei.academy.service.user.IUserService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("/signUp")
    public ResponseEntity<?> register(@Validated @RequestBody SignUpForm signUpForm){
        if (userService.existsByUserName(signUpForm.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("fail, username existed"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("fail, email existed"), HttpStatus.OK);
        }
        User user = new User();
        user.setName(signUpForm.getName());
        user.setUserName(signUpForm.getUsername());
        user.setEmail(signUpForm.getEmail());
        user.setPassword(signUpForm.getPassword());
        Set<String> strRole = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRole.forEach(role-> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(String.valueOf(RoleName.ADMIN))
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    Role pmRole = roleService.findByName(String.valueOf(RoleName.PM))
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleService.findByName(String.valueOf(RoleName.USER))
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }

        });
        user.setRoles(roles);
        userService.save(user);
        System.out.println("user"+user.toString());
        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@Validated @RequestBody SignInForm signInForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInForm.getUsername(),signInForm.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt,userPrinciple.getUserName(),userPrinciple.getId(),userPrinciple.getName(),userPrinciple.getEmail(),userPrinciple.getAuthorities()));
    }
}
