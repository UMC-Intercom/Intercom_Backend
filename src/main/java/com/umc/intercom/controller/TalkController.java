package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.common.enums.Status;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.service.TalkService;
import com.umc.intercom.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talks")
public class TalkController {

    private final TalkService talkService;
    private final UserService userService;

    // talk 생성
    @Operation(summary = "톡톡 게시글 작성", description = "Content-Type을 multipart/form-data 형식으로 보내주세요.\n\n" +
                "id는 임시저장된 글을 저장하는 경우만 보내주세요.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<TalkDto.TalkResponseDto> createTalk(@RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                              @RequestParam(name = "id", required = false) Long id,
                                                              @RequestParam(name = "title") String title,
                                                              @RequestParam(name = "content") String content,
                                                              @RequestParam(name = "category") String category) {

        TalkDto.TalkRequestDto talkRequestDto = TalkDto.TalkRequestDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .build();

        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();

        TalkDto.TalkResponseDto createdTalkDto = talkService.createTalk(talkRequestDto, files, userEmail, Status.SAVED);
        return new ResponseEntity<>(createdTalkDto, HttpStatus.CREATED);
    }

    @Operation(summary = "톡톡 게시글 임시저장", description = "Content-Type을 multipart/form-data 형식으로 보내주세요.")
    @PostMapping(value = "/temporary-save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<TalkDto.TalkResponseDto> temporarySaveTalk(@RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                              @RequestParam(name = "title") String title,
                                                              @RequestParam(name = "content") String content,
                                                              @RequestParam(name = "category") String category) {

        TalkDto.TalkRequestDto talkRequestDto = TalkDto.TalkRequestDto.builder()
                .title(title)
                .content(content)
                .category(category)
                .build();

        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();

        TalkDto.TalkResponseDto createdTalkDto = talkService.createTalk(talkRequestDto, files, userEmail, Status.TEMPORARY_SAVED);
        return new ResponseEntity<>(createdTalkDto, HttpStatus.CREATED);
    }

    // talk 최신 순 리스트 조회(페이징 처리)
    @Operation(summary = "최신 순으로 톡톡 게시글 목록 조회")
    @GetMapping
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalks(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalks(page);

        // 스웨거 테스트 시 확인
//        System.out.println("현재 페이지 데이터 수(범위 넘어가면 0): " + talkDtoPage.getContent().size());
//        System.out.println("현재 페이지 번호: " + (talkDtoPage.getNumber() + 1));
//        System.out.println("총 페이지 수: " + talkDtoPage.getTotalPages());
//        System.out.println("총 데이터 수: " + talkDtoPage.getTotalElements());

        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 조회수 순 리스트 조회(페이징 처리)
    @Operation(summary = "조회수 많은 순으로 톡톡 게시글 목록 조회")
    @GetMapping("/view-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalksByViewCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalksByViewCounts(page);
        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 조회수 순 리스트 조회(페이징 처리)
    @Operation(summary = "좋아요 많은 순으로 톡톡 게시글 목록 조회")
    @GetMapping("/like-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalksByLikeCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalksByLikeCounts(page);
        return ResponseEntity.ok(talkDtoPage);
    }

    @Operation(summary = "댓글 많은 순으로 톡톡 게시글 목록 조회")
    @GetMapping("/comment-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getTalksByCommentCounts(@RequestParam(defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> resultPage = talkService.getTalksByCommentCounts(page);
        return ResponseEntity.ok(resultPage);
    }

    // talk 상세 조회
    @Operation(summary = "톡톡 게시글 상세 조회", description = "{id} 자리에 상세 조회할 톡톡 게시글 id를 전달해주세요.")
    @GetMapping("/{id}")
    public ResponseEntity<TalkDto.TalkResponseDto> getTalkById(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Optional<TalkDto.TalkResponseDto> optionalTalkDto = talkService.getTalkById(userEmail, id);

        return optionalTalkDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 제목으로 talk 검색
    @Operation(summary = "톡톡 게시글 검색", description = "제목으로 검색")
    @GetMapping("/search")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> searchTalksByTitleAndStatus(@RequestParam("title") String title,
                                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkPageDto = talkService.searchTalksByTitleAndStatus(title, page);
        return ResponseEntity.ok(talkPageDto);
    }

    @Operation(summary = "회원이 임시저장한 톡톡 조회", description = "임시 저장은 하나만 가능\n\n" +
                "임시저장된 톡톡 존재하면 상세 정보 반환, 존재하지 않으면 HTTP 상태 코드 204 (No Content)를 반환")
    @GetMapping("/temporary-save")
    public ResponseEntity<TalkDto.TalkResponseDto> getTemporarilySavedTalk() {
        String userEmail = SecurityUtil.getCurrentUsername();

        TalkDto.TalkResponseDto temporarilySavedTalk = talkService.getTemporarilySavedTalk(userEmail);
        if (temporarilySavedTalk == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(temporarilySavedTalk);
    }

    @Operation(summary = "현직자 인증", description = "서버 응답으로 선택한 분야 반환(String)")
    @PostMapping("/certification-mentor")
    public ResponseEntity<String> certificationMentor(@RequestParam(value = "mentorField") String mentorField) {
        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();
        String field = userService.certificationMentor(userEmail, mentorField);
        return new ResponseEntity<>(field, HttpStatus.CREATED);
    }

    @Operation(summary = "현직자 여부 조회", description = "톡톡 작성하기 클릭 시 호출 -> 현직자면 '잠시만요, 현직자이신가요?' 팝업창 띄우지 않기\n\n" +
            "현직자면 해당 분야 string으로 반환(ex. IT개발·데이터), 현직자가 아니면 null 반환")
    @PostMapping("/check-mentor")
    public ResponseEntity<String> checkIfUserIsMentor() {
        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();
        String field = userService.checkIfUserIsMentor(userEmail);
        return new ResponseEntity<>(field, HttpStatus.CREATED);
    }
}
