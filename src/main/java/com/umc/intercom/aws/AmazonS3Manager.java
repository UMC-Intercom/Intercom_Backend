package com.umc.intercom.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.intercom.config.AmazonConfig;
import com.umc.intercom.domain.Uuid;
import com.umc.intercom.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    private final UuidRepository uuidRepository;

    // 어떤 디렉토리의 어떤 식별자인지는 KeyName으로 지정
    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    // KeyName을 만들어서 리턴 해주는 메서드
    // talk 디렉토리
    public String generateTalkKeyName(Uuid uuid) {
        return amazonConfig.getTalkPath() + '/' + uuid.getUuid();
    }

    // post 디렉토리
    public String generatePostKeyName(Uuid uuid) {
        return amazonConfig.getPostPath() + '/' + uuid.getUuid();
    }
}