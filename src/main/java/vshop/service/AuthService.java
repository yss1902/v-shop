package vshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vshop.crypto.PasswordEncoder;
import vshop.domain.User;
import vshop.exception.AlreadyExistsEmailException;
import vshop.exception.InvalidSigninInformation;
import vshop.repository.UserRepository;
import vshop.request.Login;
import vshop.request.Signup;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signin(Login login){
        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        var matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
        if(!matches){
            throw new InvalidSigninInformation();
        }
        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if(userOptional.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
