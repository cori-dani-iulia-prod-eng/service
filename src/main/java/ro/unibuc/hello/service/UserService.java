package ro.unibuc.hello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.unibuc.hello.data.UserEntity;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.User;
import ro.unibuc.hello.dto.UserUpdate;
import ro.unibuc.hello.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public List<User> getAll() {
        return userRepository.findAll()
                .stream()
                .map(User::new)
                .collect(Collectors.toList());
    }

    public User get(String id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return new User(entity);
    }

    public UserUpdate update(UserUpdate user) {
        UserEntity entity = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(user.getId()));
        if(!user.getName().isEmpty()) {
            entity.setName(user.getName());
        }
        if(!user.getEmail().isEmpty()) {
            entity.setEmail(user.getEmail());
        }
        if(!user.getPhone().isEmpty()) {
            entity.setPhone(user.getPhone());
        }
        UserEntity savedUser = userRepository.save(entity);
        return new UserUpdate(savedUser);
    }

    public void delete(String id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        userRepository.delete(entity);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }
}
