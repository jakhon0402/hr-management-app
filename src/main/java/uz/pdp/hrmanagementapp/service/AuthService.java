package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.entity.enums.RoleName;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.LoginDto;
import uz.pdp.hrmanagementapp.payload.RegisterDto;
import uz.pdp.hrmanagementapp.repository.RoleRepo;
import uz.pdp.hrmanagementapp.repository.UserRepo;
import uz.pdp.hrmanagementapp.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepo roleRepo;

    @Lazy
    @Autowired
    JavaMailSender javaMailSender;

    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;



    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepo.findByEmailAndEmailCode(email, emailCode);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEmailCode(null);
            user.setEnabled(true);
            userRepo.save(user);
            return new ApiResponse("Akkount tasdiqlandi!",true);
        }

        return new ApiResponse("Akkount allaqachon tasdiqlangan!",false);

    }

    public ApiResponse login(LoginDto loginDto){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getEmail(), user.getRoles());
            return new ApiResponse("Token",true,token);
        }
        catch (BadCredentialsException badCredentialsException){
            return new ApiResponse("Parol yoki login xato",false);

        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepo.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException(username + " topilmadi"));
    }
}
