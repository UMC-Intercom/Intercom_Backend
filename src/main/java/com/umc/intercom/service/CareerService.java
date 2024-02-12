package com.umc.intercom.service;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.repository.CareerRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CareerService {
    UserRepository userRepository;
    CareerRepository careerRepository;

    @Transactional
    public CareerDto.CareerResponseDto createCareer(CareerDto.CareerRequestDto careerDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        Career career = Career.builder()
                .university(careerDto.getUniversity())
                .major(careerDto.getMajor())
                .skill(careerDto.getSkill())
                .user(user.orElseThrow(()-> new RuntimeException("User not Found")))
                .build();

        Career createdCareer = careerRepository.save(career);

        return CareerDto.CareerResponseDto.toDto(createdCareer);
    }

    public List<CareerDto.CareerResponseDto> getCareerByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new RuntimeException("User Not Found");
        }
        List<Career> careers = careerRepository.findByUser(user.get());

        return careers.stream().map(career -> {
            return CareerDto.CareerResponseDto.toDto(career);
        }).collect(Collectors.toList());
    }

    public void updateCareer(Long id, String userEmail, CareerDto.CareerRequestDto requestDto){
        Career careerToUpdate = careerRepository.findByid(id)
                .orElseThrow(() -> new RuntimeException("Career를 찾을 수 없습니다."));

        String careerOwner = careerToUpdate.getUser().getEmail();
        if(!careerOwner.equals(userEmail))
            throw new RuntimeException("본인이 작성한 Career만 수정 가능합니다. ");

        careerToUpdate.setEnglish(requestDto.getEnglish());
        careerToUpdate.setScore(requestDto.getScore());
        careerToUpdate.setCertification(requestDto.getCertification());
        careerToUpdate.setUniversity(requestDto.getUniversity());
        careerToUpdate.setMajor(requestDto.getMajor());
        careerToUpdate.setSkill(requestDto.getSkill());
        careerToUpdate.setGpa(requestDto.getGpa());
        careerToUpdate.setActivity(requestDto.getActivity());
        careerToUpdate.setSkill(requestDto.getSkill());
        careerToUpdate.setLink(requestDto.getLink());

        careerRepository.save(careerToUpdate);
    }
}
