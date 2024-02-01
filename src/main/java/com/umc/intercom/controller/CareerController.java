package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/careers")
public class CareerController {
    private final CareerService careerService;

    @PostMapping
    public ResponseEntity<CareerDto> createCareer(@RequestBody CareerDto careerDto){
        String userEmail = SecurityUtil.getCurrentUsername();

        CareerDto createdCareerDto = careerService.createCareer(careerDto, userEmail);
        return new ResponseEntity<>(createdCareerDto, HttpStatus.CREATED);
    }
}
