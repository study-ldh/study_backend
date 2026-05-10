package com.kms.study;

import com.kms.study.domain.StudyItem;
import com.kms.study.repository.StudyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(StudyRepository repository) {
        return args -> {
            repository.save(StudyItem.builder().title("리액트 설정").content("Node.js 설치 및 프로젝트 생성").build());
            repository.save(StudyItem.builder().title("스프링 부트 설정").content("MySQL 및 JPA 연동 완료").build());
            System.out.println("--- 초기 테스트 데이터 생성 완료 ---");
        };
    }
}