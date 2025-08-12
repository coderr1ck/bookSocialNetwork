package com.coderrr1ck.bookBackend.service;

import com.coderrr1ck.bookBackend.authDTOs.AuthRequestHandler;
import com.coderrr1ck.bookBackend.authDTOs.AuthResponseHandler;
import com.coderrr1ck.bookBackend.exceptions.EmailAlreadyExists;
import com.coderrr1ck.bookBackend.exceptions.EmailNotSentException;
import com.coderrr1ck.bookBackend.exceptions.RoleNotFoundException;
import com.coderrr1ck.bookBackend.exceptions.TokenNotFoundException;
import com.coderrr1ck.bookBackend.models.Token;
import com.coderrr1ck.bookBackend.models.User;
import com.coderrr1ck.bookBackend.repository.RoleRepository;
import com.coderrr1ck.bookBackend.repository.TokenRepository;
import com.coderrr1ck.bookBackend.repository.UserRepository;
import com.coderrr1ck.bookBackend.response.GenericResponseHandler;
import com.coderrr1ck.bookBackend.securityConfig.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final String ACCOUNT_ACTIVATION = "Account Activation";
    @Value("${application.mailing.frontend.activation-url}")
    private String CONFIRMATION_URL ;
    private final GenericResponseHandler genericResponseHandler;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder,
                                 GenericResponseHandler genericResponseHandler,
                                 EmailService emailService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericResponseHandler = genericResponseHandler;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> registerUser(AuthRequestHandler registerRequestData) {
        if(userRepository.existsByEmail(registerRequestData.getEmail())) {
            throw new EmailAlreadyExists("Email already exists: " + registerRequestData.getEmail());
        }
        var userRoles = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found: USER"));
        var user  = User.builder()
                .email(registerRequestData.getEmail())
                .password(passwordEncoder.encode(registerRequestData.getPassword()))
                .firstname(registerRequestData.getFirstName())
                .lastname(registerRequestData.getLastName())
                .dob(LocalDate.parse(registerRequestData.getDob()))
                .isLocked(false)
                .isActive(false)
                .createdAt(LocalDate.now())
                .roles(List.of(userRoles))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
        // send validation email to user
        return genericResponseHandler.setResponse("User Registered Successfully");
    }


    public ResponseEntity<?> loginUser(AuthRequestHandler loginRequestData){
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestData.getEmail(),
                        loginRequestData.getPassword()
                )
        ); // returns authentication object
        System.out.println(auth);
        var user = ((User)auth.getPrincipal());
        var claims = new HashMap<String,Object>();
        claims.put("fullName",user.getFullName());
        var jwtToken = jwtService.generateTokenWithClaimsAndUser(claims,user);
        AuthResponseHandler response = new AuthResponseHandler();
        response.setToken(jwtToken);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> activateAccount(String code){
        Token token = tokenRepository.findByToken(code).orElseThrow(()->
                new TokenNotFoundException("Invalid Token")
        );
        if(token.getExpiresAt().isAfter(LocalDateTime.now())){
                var user = userRepository.findById(token.getUser().getId())
                        .orElseThrow(()->new UsernameNotFoundException("User not found "));
                user.setIsActive(true);
                userRepository.save(user);
                token.setValidatedAt(LocalDateTime.now());
                tokenRepository.save(token);
                return genericResponseHandler.setResponse("Account Activated Successfully.");
        }else {
            sendValidationEmail(token.getUser());
            tokenRepository.deleteById(token.getId());
            return genericResponseHandler.setResponse("Token expired.");
        }
    }

    private void sendValidationEmail(User user) {
//        here we not only send validation email to user , we also have to generate and store
//        activation token and store it in out db and send it over the mail to cross check it ,
//        when user trires to activate his/her account.
        String generatedToken = generateAndSaveActivationToken(user);
        try {
            emailService.sendEmail(
                    user.getEmail(),
                    user.getUsername(),
                    generatedToken,
                    ACCOUNT_ACTIVATION,
                    CONFIRMATION_URL+"/"+generatedToken);
        } catch (MessagingException e) {
            throw new EmailNotSentException(e.getMessage());
        }

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationToken(6);
        var token = Token
                .builder()
                .token(generatedToken)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length){
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }


}
