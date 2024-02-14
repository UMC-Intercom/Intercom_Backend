package com.umc.intercom.service;

import com.umc.intercom.aws.AmazonS3Manager;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.Uuid;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import com.umc.intercom.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;

    public UserDto.FindEmailResponseDto findEmailByPhone(UserDto.FindEmailRequestDto requestDto) {
        User user = userRepository.findUserByPhone(requestDto.getPhone());
        return new UserDto.FindEmailResponseDto(user.getName(), user.getEmail());
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
        }
    }

    public String saveProfile(MultipartFile file, String userEmail, String category) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // 이미지 업로드
        String pictureUrl = null;
        if (file != null){
            String uuid = UUID.randomUUID().toString();
            Uuid savedUuid = uuidRepository.save(Uuid.builder()
                    .uuid(uuid).build());
            pictureUrl = s3Manager.uploadFile(s3Manager.generateProfileKeyName(savedUuid), file);
        }

        System.out.println("s3 url(클릭 시 브라우저에 사진 뜨는지 확인): " + pictureUrl);

        // 기존 이미지가 존재하면 S3에서 삭제 후 새 이미지 저장
        if (category.equals("careerProfile")) {     // 커리어 프로필
            String oldProfileUrl = user.get().getCareerProfile();
            if (oldProfileUrl != null) {
                s3Manager.deleteFile(oldProfileUrl);
            }

            user.get().setCareerProfile(pictureUrl);
        }
        else {      // 기본 프로필
            String oldProfileUrl = user.get().getDefaultProfile();
            if (oldProfileUrl != null) {
                s3Manager.deleteFile(oldProfileUrl);
            }

            user.get().setDefaultProfile(pictureUrl);
        }

        userRepository.save(user.get());

        return pictureUrl;
    }

    public String getProfile(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return user.get().getDefaultProfile();
    }

    public String certificationMentor(String userEmail, String mentorField) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        user.get().setMentorField(mentorField);
        userRepository.save(user.get());

        return user.get().getMentorField();
    }
}
