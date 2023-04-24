package com.eray.jwtExample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // token i gecerli kullanicilar icin onlari karsilayacak bir dashboard tanimladik
    @GetMapping
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok("Welcome Dashboard");
    }
}
