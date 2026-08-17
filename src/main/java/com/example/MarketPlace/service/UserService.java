package com.example.MarketPlace.service;


import com.example.MarketPlace.dto.UserRegisterDto;
import com.example.MarketPlace.dto.UserResponseDto;
import com.example.MarketPlace.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import com.example.MarketPlace.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto register(UserRegisterDto dto){
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new IllegalArgumentException("Такий email уже існує.");
        }

        User user = new User();
        user.setEmail(dto.getEmail()); // Не забудь додати збереження email
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Хешуємо пароль
        user.setFirst_name(dto.getFirst_name());
        user.setLast_name(dto.getLast_name());

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirst_name(),
                savedUser.getLast_name()
        );

    }
    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id){
        User user = userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Користувача не знайдено"));


        return  new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name()
        );
    }
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUserProfile(Long userId) {

        return getById(userId);
    }

}
