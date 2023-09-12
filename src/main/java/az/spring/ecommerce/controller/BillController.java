package az.spring.ecommerce.controller;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.request.BillRequest;
import az.spring.ecommerce.service.BillService;
import az.spring.ecommerce.utils.CommerceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/generateReport")
    public ResponseEntity<String> generateReport(@RequestBody Map<String, Object> billRequest) {
        try {
            return billService.generateReport(billRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}