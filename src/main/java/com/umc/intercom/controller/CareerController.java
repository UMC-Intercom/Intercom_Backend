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

    @Operation(summary = "내 커리어 작성")
    @PostMapping
    public ResponseEntity<CareerDto.CareerResponseDto> createCareer(@RequestBody CareerDto.CareerRequestDto careerRequestDto){
        String userEmail = SecurityUtil.getCurrentUsername();

        CareerDto.CareerResponseDto createdCareerDto = careerService.createCareer(careerRequestDto, userEmail);
        return new ResponseEntity<>(createdCareerDto, HttpStatus.CREATED);
    }

    @Operation(summary = "이메일로 커리어 조회")
    @GetMapping("/{userEmail}")
    public ResponseEntity<List<CareerDto.CareerResponseDto>> getCareerByEmail(@PathVariable String userEmail) {
        List<CareerDto.CareerResponseDto> careerDtos = careerService.getCareerByEmail(userEmail);

        return ResponseEntity.ok(careerDtos);
    }

    @Operation(summary = "아이디로 커리어 수정")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCareer(@PathVariable Long id, @RequestBody CareerDto.CareerRequestDto careerRequestDto){
        String userEmail = SecurityUtil.getCurrentUsername();
        careerService.updateCareer(id, userEmail, careerRequestDto);
        return ResponseEntity.noContent().build();
    }
}
