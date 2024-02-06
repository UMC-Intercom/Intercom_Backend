package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.service.CareerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{userEmail}")
    public ResponseEntity<List<CareerDto>> getCareerByEmail(@PathVariable String userEmail) {
        List<CareerDto> careerDtos = careerService.getCareerByEmail(userEmail);

        return ResponseEntity.ok(careerDtos);
    }
}
