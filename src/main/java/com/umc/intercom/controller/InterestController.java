package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @Operation(summary = "관심분야 등록", description = "회원가입 직후 로그인 하지 않은 상태에서 api 호출됨. -> 회원가입 시 서버 응답으로 반환된 회원의 닉네임을 함께 전달해주세요." +
                                        "\n\n관심분야는 리스트로 전달해주세요. (ex. [\"IT개발·데이터\", \"마케팅·홍보·조사\", \"디자인\"] )")
    @PostMapping
    public ResponseEntity<Void> addInterest(@RequestParam("nickname") String nickname, @RequestBody List<String> interests) {
        interestService.addInterestsByNickname(nickname, interests);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관심분야 조회")
    @GetMapping
    public ResponseEntity<List<String>> getInterests() {
        String userEmail = SecurityUtil.getCurrentUsername();
        List<String> interests = interestService.getInterests(userEmail);
        return ResponseEntity.ok(interests);
    }

    @Operation(summary = "관심분야 수정", description = "관심분야는 리스트로 전달해주세요. (ex. [\"IT개발·데이터\", \"마케팅·홍보·조사\", \"디자인\"] " +
                                        "\n\n빈 리스트 전달 시 해당 사용자의 관심분야가 모두 삭제됨.")
    @PutMapping
    public ResponseEntity<Void> updateInterests(@RequestBody List<String> interests) {
        String userEmail = SecurityUtil.getCurrentUsername();
        interestService.updateInterests(userEmail, interests);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관심분야 등록 여부 확인", description = "회원가입 시 건너뛰기 버튼 누르면, 추후 로그인 시 관심분야 입력창 다시 띄우도록 등록 여부 확인" +
                                                "\n\n등록했으면 true, 등록하지 않았으면 false를 반환")
    @GetMapping("/hasInterests")
    public ResponseEntity<Boolean> hasInterests() {
        String userEmail = SecurityUtil.getCurrentUsername();
        boolean hasInterests = interestService.hasInterests(userEmail);
        return ResponseEntity.ok(hasInterests);
    }

}
