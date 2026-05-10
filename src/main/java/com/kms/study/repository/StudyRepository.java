package com.kms.study.repository;

import com.kms.study.domain.StudyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<StudyItem, Long> {
}