package com.passwordvalidation.controller;

import com.passwordvalidation.dto.LoginInfo;
import com.passwordvalidation.service.PasswordValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class PasswordValidationController {

    @Autowired
    private PasswordValidatorService passwordValidatorService;

    @Autowired
    public PasswordValidationController(PasswordValidatorService passwordValidatorService) {
        this.passwordValidatorService = passwordValidatorService;
    }

    @PostMapping("/validate")
    public String validatePassword(@RequestBody LoginInfo info) {
        if (passwordValidatorService.validate(info.getPassword())) {
            return "Password is valid";
        } else {
            return "Password is invalid";
        }
    }
}
