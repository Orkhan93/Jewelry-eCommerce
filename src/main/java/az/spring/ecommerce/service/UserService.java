package az.spring.ecommerce.service;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.mappers.UserMapper;
import az.spring.ecommerce.model.User;
import az.spring.ecommerce.repository.UserRepository;
import az.spring.ecommerce.request.UserSignUpRequest;
import az.spring.ecommerce.security.JwtRequestFilter;
import az.spring.ecommerce.security.JwtUtil;
import az.spring.ecommerce.security.UserDetailServiceImpl;
import az.spring.ecommerce.utils.CommerceUtil;
import az.spring.ecommerce.utils.EmailUtil;
import az.spring.ecommerce.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtUtil jwtUtil;
    private final JwtRequestFilter jwtRequestFilter;
    private final EmailUtil emailUtil;

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

    public ResponseEntity<List<UserWrapper>> getAllUser() {
        log.info("Inside getAllUser {}", getAllUser());
        try {
            if (jwtRequestFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> update(UserSignUpRequest userSignUpRequest) {
        try {
            if (jwtRequestFilter.isAdmin()) {
                Optional<User> optionalUser = userRepository.findById(userSignUpRequest.getId());
                if (!optionalUser.isEmpty()) {
                    userRepository.updateStatus(userSignUpRequest.getStatus(), userSignUpRequest.getId());
                    sendMailToAllAdmin(userSignUpRequest.getStatus(),
                            optionalUser.get().getEmail(), userRepository.getAllAdmin());
                    return CommerceUtil.getResponseMessage
                            ("User Status Updated Successfully.", HttpStatus.OK);
                 
                    sendMailToAllAdmin(userSignUpRequest.getStatus(), optionalUser.get().getEmail(), userRepository.getAllAdmin());
                    return CommerceUtil.getResponseMessage("User Status Updated Successfully.", HttpStatus.OK);

                } else {
                    CommerceUtil.getResponseMessage("User id doesn't exist.", HttpStatus.OK);
                }
            } else {
                return CommerceUtil.getResponseMessage(CommerceConstant.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> checkToken() {
        return CommerceUtil.getResponseMessage("true", HttpStatus.OK);
    }

    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObject = userRepository.findByEmail(jwtRequestFilter.getCurrentUser());
            if (userObject != null) {
                if (userObject.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObject.setPassword(requestMap.get("newPassword"));
                    userRepository.save(userObject);
                    return CommerceUtil.getResponseMessage("Password Updated Successfully.", HttpStatus.OK);
                }
                return CommerceUtil.getResponseMessage("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CommerceUtil.getResponseMessage
                    (CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> forgotPassword(UserSignUpRequest userSignUpRequest) {
        try {
            User user = userRepository.findByEmail(userSignUpRequest.getEmail());
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtil.forgetMail(user.getEmail(), "Credentials by E-commerce", user.getPassword());
            return CommerceUtil.getResponseMessage("Check your mail for Credentials.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validationSignUp(UserSignUpRequest signUpRequest) {
        log.info("Inside signUpRequest {}", signUpRequest);
        if (signUpRequest.getName() != null && signUpRequest.getEmail() != null
                && signUpRequest.getContactNumber() != null && signUpRequest.getPassword() != null) {
            return true;
        }
        return false;
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtRequestFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtil.sendSimpleMessage(jwtRequestFilter.getCurrentUser(),
                    "Account Approved.", "USER:- " + user + "\n is approved by \nADMIN:-"
                            + jwtRequestFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtil.sendSimpleMessage(jwtRequestFilter.getCurrentUser(),
                    "Account Disabled.", "USER:- " + user + "\n is disabled by \nADMIN:-"
                            + jwtRequestFilter.getCurrentUser(), allAdmin);
        }
    }

    public ResponseEntity<String> checkToken() {
        return CommerceUtil.getResponseMessage("true", HttpStatus.OK);
    }

    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObject = userRepository.findByEmail(jwtRequestFilter.getCurrentUser());
            if (userObject != null) {
                if (userObject.getPassword().equals(requestMap.get("oldPassword"))) {
                    userObject.setPassword(requestMap.get("newPassword"));
                    userRepository.save(userObject);
                    return CommerceUtil.getResponseMessage("Password Updated Successfully.", HttpStatus.OK);
                }
                return CommerceUtil.getResponseMessage("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CommerceUtil.getResponseMessage
                    (CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> forgotPassword(UserSignUpRequest userSignUpRequest) {
        try {
            User user = userRepository.findByEmail(userSignUpRequest.getEmail());
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtil.forgetMail(user.getEmail(), "Credentials by E-commerce", user.getPassword());
            return CommerceUtil.getResponseMessage("Check your mail for Credentials.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validationSignUp(UserSignUpRequest signUpRequest) {
        log.info("Inside signUpRequest {}", signUpRequest);
        if (signUpRequest.getName() != null && signUpRequest.getEmail() != null
                && signUpRequest.getContactNumber() != null && signUpRequest.getPassword() != null) {
            return true;
        }
        return false;
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtRequestFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtil.sendSimpleMessage(jwtRequestFilter.getCurrentUser(),
                    "Account Approved.", "USER:- " + user + "\n is approved by \nADMIN:-"
                            + jwtRequestFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtil.sendSimpleMessage(jwtRequestFilter.getCurrentUser(),
                    "Account Disabled.", "USER:- " + user + "\n is disabled by \nADMIN:-"
                            + jwtRequestFilter.getCurrentUser(), allAdmin);
        }
    }

}