package com.umc.intercom.service;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.CareerDetail;
import com.umc.intercom.domain.Spec;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.CareerDto;
import com.umc.intercom.repository.CareerDetailRepository;
import com.umc.intercom.repository.CareerRepository;
import com.umc.intercom.repository.SpecRepository;
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
    CareerDetailRepository careerDetailRepository;
    SpecRepository specRepository;

    @Transactional
    public CareerDto.CareerResponseDto createCareer(CareerDto.CareerRequestDto careerDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        Career career = Career.builder()
                .university(careerDto.getUniversity())
                .major(careerDto.getMajor())
                .skill(careerDto.getSkill())
                .title(careerDto.getTitle())
                .content(careerDto.getContent())
                .user(user.orElseThrow(()-> new RuntimeException("User not Found")))
                .build();

//        career.getUser().setNickname(user.get().getNickname());

        CareerDetail careerDetail = CareerDetail.builder()
                .career(career)
                .company(careerDto.getCompany())
                .position(careerDto.getPosition())
                .salary(careerDto.getSalary())
                .job(careerDto.getJob())
                .startDate(careerDto.getStartDate())
                .endDate(careerDto.getEndDate())
                .build();


        Spec spec = Spec.builder()
                .career(career)
                .certification(careerDto.getCertification())
                .activity(careerDto.getActivity())
                .award(careerDto.getAward())
                .globalExp(careerDto.getGlobalExp())
                .volunteer(careerDto.getVolunteer())
                .gpa(careerDto.getGpa())
                .build();

        Career createdCareer = careerRepository.save(career);
        CareerDetail createdCareerDetail = careerDetailRepository.save(careerDetail);
        Spec createdSpec = specRepository.save(spec);


        return CareerDto.CareerResponseDto.toDto(createdCareer, createdCareerDetail, createdSpec);
    }

    public List<CareerDto.CareerResponseDto> getCareerByEmail(String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (!user.isPresent()) {
            throw new RuntimeException("User Not Found");
        }

        List<Career> careers = careerRepository.findByUser(user.get());

        return careers.stream().map(career -> {
            CareerDetail careerDetail = careerDetailRepository.findByCareer(career)
                    .orElseThrow(() -> new RuntimeException("CareerDetail Not Found"));
            Spec spec = specRepository.findByCareer(career)
                    .orElseThrow(() -> new RuntimeException("Spec Not Found"));

            return CareerDto.CareerResponseDto.toDto(career, careerDetail, spec);
        }).collect(Collectors.toList());

    }
}
