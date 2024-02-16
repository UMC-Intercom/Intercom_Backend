package com.umc.intercom.service;

import com.umc.intercom.domain.Activity;
import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.repository.ActivityRepository;
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
    ActivityRepository activityRepository;

    @Transactional
    public CareerDto.CareerResponseDto createCareer(CareerDto.CareerRequestDto careerDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);

        Career career = Career.builder()
                .english(careerDto.getEnglish())
                .score(careerDto.getScore())
                .certification(careerDto.getCertification())
                .university(careerDto.getUniversity())
                .major(careerDto.getMajor())
                .gpa(careerDto.getGpa())
                .skill(careerDto.getSkill())
                .link(careerDto.getLink())
                .user(user.orElseThrow(()-> new RuntimeException("User not Found")))
                .build();

        Career createdCareer = careerRepository.save(career);

        List<Activity> activities = careerDto.getActivity().stream()
                .map(activityDto -> new Activity(
                        activityDto.getName(),
                        activityDto.getStartDate(),
                        activityDto.getEndDate(),
                        activityDto.getDescription()
                ))
                .collect(Collectors.toList());

        activities.forEach(activity -> activity.setCareer(createdCareer));
        List<Activity> createdActivities = activityRepository.saveAll(activities);

        return CareerDto.CareerResponseDto.toDto(createdCareer, createdActivities.stream()
                .map(CareerDto.ActivityDto::toDto)
                .collect(Collectors.toList()));
    }

    public CareerDto.CareerResponseDto getCareerByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User Not Found");
        }
        Optional<Career> career = careerRepository.findByUser(user.get());
        if (career.isEmpty()) {
            return null;
        }

        List<Activity> createdActivities = activityRepository.findByCareer(career.get());

        List<CareerDto.ActivityDto> activityDtoList = createdActivities.stream()
                .map(CareerDto.ActivityDto::toDto)
                .collect(Collectors.toList());

        return CareerDto.CareerResponseDto.toDto(career.get(), activityDtoList);
    }

    public CareerDto.CareerResponseDto updateCareer(String userEmail, CareerDto.CareerRequestDto requestDto){
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new RuntimeException("User Not Found");
        }

        // 내역이 없으면 생성, 있으면 수정 진행
        Optional<Career> optionalCareer = careerRepository.findByUser(user.get());
        if (optionalCareer.isEmpty()) {
            return createCareer(requestDto, userEmail);
        }

        Career careerToUpdate = optionalCareer.get();

        String careerOwner = careerToUpdate.getUser().getEmail();
        if(!careerOwner.equals(userEmail))
            throw new RuntimeException("본인이 작성한 Career만 수정 가능합니다.");

        // 기존의 Activity 모두 삭제
        List<Activity> existingActivities = activityRepository.findByCareer(careerToUpdate);
        activityRepository.deleteAll(existingActivities);

        List<Activity> updatedActivities = requestDto.getActivity().stream()
                .map(activityDto -> new Activity(
                        activityDto.getName(),
                        activityDto.getStartDate(),
                        activityDto.getEndDate(),
                        activityDto.getDescription()
                ))
                .collect(Collectors.toList());

        updatedActivities.forEach(activity -> activity.setCareer(careerToUpdate));
        List<Activity> createdActivities = activityRepository.saveAll(updatedActivities);

        careerToUpdate.setEnglish(requestDto.getEnglish());
        careerToUpdate.setScore(requestDto.getScore());
        careerToUpdate.setCertification(requestDto.getCertification());
        careerToUpdate.setUniversity(requestDto.getUniversity());
        careerToUpdate.setMajor(requestDto.getMajor());
        careerToUpdate.setSkill(requestDto.getSkill());
        careerToUpdate.setGpa(requestDto.getGpa());
        careerToUpdate.setSkill(requestDto.getSkill());
        careerToUpdate.setLink(requestDto.getLink());

        Career updatedCareer = careerRepository.save(careerToUpdate);

        List<CareerDto.ActivityDto> activityDtoList = createdActivities.stream()
                .map(CareerDto.ActivityDto::toDto)
                .collect(Collectors.toList());

        return CareerDto.CareerResponseDto.toDto(updatedCareer, activityDtoList);
    }
}
