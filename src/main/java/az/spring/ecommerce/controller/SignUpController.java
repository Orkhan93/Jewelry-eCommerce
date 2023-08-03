package az.spring.ecommerce.controller;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.request.UserRequest;
import az.spring.ecommerce.request.UserSignUpRequest;
import az.spring.ecommerce.service.UserService;
import az.spring.ecommerce.utils.CommerceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SignUpController {

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


// kohne versiya
//        if (userService.hasUserWithEmail(userSignUpRequest.getEmail())) {
//            return CommerceUtil.getResponseMessage(CommerceConstant.USER_ALREADY_EXITS, HttpStatus.NOT_ACCEPTABLE);
//        }
//        UserRequest createdUser = userService.signUp(userSignUpRequest);
//        if (createdUser == null) {
//            return new ResponseEntity<>("User not created. Come again later!", HttpStatus.BAD_REQUEST);
//        }
//        return CommerceUtil.getResponseMessage(CommerceConstant.SUCCESSFULLY_REGISTER, HttpStatus.CREATED);
//    }

}