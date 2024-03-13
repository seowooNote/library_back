package com.study.library.controller;

import com.study.library.aop.annotation.ParamsPrintAspect;
import com.study.library.dto.SignupReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // [/auth/singup] 상위주소
public class AuthController {

    @ParamsPrintAspect
    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody SignupReqDto signupReqDto) {
        return ResponseEntity.ok(null);
    }

}
