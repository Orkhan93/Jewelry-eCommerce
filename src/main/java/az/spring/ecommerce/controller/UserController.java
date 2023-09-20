package az.spring.ecommerce.controller;

import az.spring.ecommerce.request.UserSignUpRequest;
import az.spring.ecommerce.service.UserService;
import az.spring.ecommerce.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
            return userService.signUp(userSignUpRequest);
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserSignUpRequest userSignUpRequest) {
            return userService.login(userSignUpRequest);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser() {
            return userService.getAllUser();
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody UserSignUpRequest userSignUpRequest) {
            return userService.update(userSignUpRequest);
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken() {
            return userService.checkToken();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> requestMap) {
            return userService.changePassword(requestMap);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserSignUpRequest userSignUpRequest) {
            return userService.forgotPassword(userSignUpRequest);
    }

}