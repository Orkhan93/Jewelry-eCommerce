package az.spring.ecommerce.service;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.mappers.UserMapper;
import az.spring.ecommerce.model.User;
import az.spring.ecommerce.repository.UserRepository;
import az.spring.ecommerce.request.UserSignUpRequest;
import az.spring.ecommerce.security.JwtUtil;
import az.spring.ecommerce.security.UserDetailServiceImpl;
import az.spring.ecommerce.utils.CommerceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> signUp(UserSignUpRequest userSignUpRequest) {
        log.info("Inside signup {}", userSignUpRequest);
        try {
            if (validationSignUp(userSignUpRequest)) {
                User user = userRepository.findFirstByEmail(userSignUpRequest.getEmail());
                if (Objects.isNull(user)) {
                    userRepository.save(userMapper.fromUserSignUpRequestToModel(userSignUpRequest)); // deyisdim !!
                    return CommerceUtil.getResponseMessage(CommerceConstant.SUCCESSFULLY_REGISTER, HttpStatus.CREATED);
                } else {
                    return CommerceUtil.getResponseMessage(CommerceConstant.USER_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CommerceUtil.getResponseMessage(CommerceConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(UserSignUpRequest userSignUpRequest) {
        log.info("Inside login {}", userSignUpRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userSignUpRequest.getEmail(), userSignUpRequest.getPassword())
            );
            if (authentication.isAuthenticated()) {
                if (userDetailService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                            jwtUtil.generateToken(userDetailService.getUserDetail().getEmail(),
                                    userDetailService.getUserDetail().getUserRole()) + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>
                            ("Wait for admin approval.", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<String>("Bad Credentials.", HttpStatus.BAD_REQUEST);
    }

    private boolean validationSignUp(UserSignUpRequest signUpRequest) {
        log.info("Inside signUpRequest {}", signUpRequest);
        if (signUpRequest.getName() != null && signUpRequest.getEmail() != null
                && signUpRequest.getContactNumber() != null && signUpRequest.getPassword() != null) {
            return true;
        }
        return false;
    }

}