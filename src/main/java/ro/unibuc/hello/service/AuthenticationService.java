package ro.unibuc.hello.service;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.unibuc.hello.config.JwtUtil;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AuthenticationRequest;
import ro.unibuc.hello.dto.AuthenticationResponse;
import ro.unibuc.hello.dto.RegisterRequest;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(@Valid RegisterRequest user) throws Exception {
        if(userRepository.existsByUsername(user.getUsername())){
           throw new Exception("User already exists");
        }
        var entity = new UserEntity(user);
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(entity);
        var jwtToken = jwtUtil.generateToken(entity);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse login(@Valid AuthenticationRequest user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        var userEntity = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername()));
        var jwtToken = jwtUtil.generateToken(userEntity);
        return new AuthenticationResponse(jwtToken);
    }
}
