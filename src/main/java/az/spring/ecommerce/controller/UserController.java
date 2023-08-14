package az.spring.ecommerce.controller;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.request.UserRequest;
import az.spring.ecommerce.request.UserSignUpRequest;
import az.spring.ecommerce.service.UserService;
import az.spring.ecommerce.utils.CommerceUtil;
import az.spring.ecommerce.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            return userService.signUp(userSignUpRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            return userService.login(userSignUpRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            return userService.update(userSignUpRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap) {
        try {
            return userService.changePassword(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserSignUpRequest userSignUpRequest) {
        try {
            return userService.forgotPassword(userSignUpRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}