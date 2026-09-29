package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.StudentDailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StudentDailyActivityRepository
        extends JpaRepository<StudentDailyActivity, String> {


    boolean existsByStudent_UserIdAndActivityDate(
            String studentId,
            LocalDate activityDate
    );


    long countByActivityDate(LocalDate activityDate);
}