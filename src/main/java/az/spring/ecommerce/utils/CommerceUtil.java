package az.spring.ecommerce.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommerceUtil {

    private CommerceUtil() {

    }

    public static ResponseEntity<String> getResponseMessage(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>(responseMessage, httpStatus);
    }

}