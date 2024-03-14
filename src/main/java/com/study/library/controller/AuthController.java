package com.study.library.controller;

import com.study.library.aop.annotation.ParamsPrintAspect;
import com.study.library.aop.annotation.ValidAspect;
import com.study.library.dto.SignupReqDto;
import com.study.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @ValidAspect
    @PostMapping("/signup")
    public ResponseEntity<?> singup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {
        if(authService.isDuplicatedByUsername(signupReqDto.getUsername())) {
            ObjectError objectError = new FieldError("username", "username", "이미 존재하는 사용자 이름입니다.");
            bindingResult.addError(objectError);
        }

        authService.signup(signupReqDto);

        return ResponseEntity.created(null).body(true);
    }

}
