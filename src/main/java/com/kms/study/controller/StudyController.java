package com.kms.study.controller;

import com.kms.study.domain.StudyItem;
import com.kms.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
// React 기본 포트(3000)에서 들어오는 요청을 허용합니다.
@CrossOrigin(origins = "http://localhost:3000")
public class StudyController {

    private final StudyRepository studyRepository;

    // React에서 'GET http://localhost:8080/api/study'로 호출
    @GetMapping("/study")
    public List<StudyItem> getStudyList() {
        return studyRepository.findAll();
    }

    // 데이터 저장을 위한 POST API (테스트용)
    @PostMapping("/study")
    public StudyItem createStudy(@RequestBody StudyItem studyItem) {
        return studyRepository.save(studyItem);
    }
}