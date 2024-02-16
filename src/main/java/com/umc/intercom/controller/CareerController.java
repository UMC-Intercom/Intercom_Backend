package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.service.CareerService;
import io.swagger.v3.oas.annotations.Operation;
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

//    @Operation(summary = "내 커리어 작성")
//    @PostMapping
//    public ResponseEntity<CareerDto.CareerResponseDto> createCareer(@RequestBody CareerDto.CareerRequestDto careerRequestDto){
//        String userEmail = SecurityUtil.getCurrentUsername();
//
//        CareerDto.CareerResponseDto createdCareerDto = careerService.createCareer(careerRequestDto, userEmail);
//        return new ResponseEntity<>(createdCareerDto, HttpStatus.CREATED);
//    }

    @Operation(summary = "커리어 조회")
    @GetMapping()
    public ResponseEntity<Object> getCareerByEmail() {
        String userEmail = SecurityUtil.getCurrentUsername();   // 로그인한 사용자 이메일
        Object careerDtos = careerService.getCareerByEmail(userEmail);

        return ResponseEntity.ok(careerDtos);
    }

    @Operation(summary = "커리어 작성 또는 수정")
    @PostMapping()
    public ResponseEntity<CareerDto.CareerResponseDto> updateCareer(@RequestBody CareerDto.CareerRequestDto careerRequestDto){
        String userEmail = SecurityUtil.getCurrentUsername();
        CareerDto.CareerResponseDto updatedCareerDto = careerService.updateCareer(userEmail, careerRequestDto);
        return new ResponseEntity<>(updatedCareerDto, HttpStatus.CREATED);
    }
}
