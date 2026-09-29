package com.campuscoin.backend.service;

import com.campuscoin.backend.entity.StudentDailyActivity;
import com.campuscoin.backend.entity.User;
import com.campuscoin.backend.repository.StudentDailyActivityRepository;
import com.campuscoin.backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StudentActivityService {

    private final StudentDailyActivityRepository activityRepository;
    private final UserRepository userRepository;

    public StudentActivityService(
            StudentDailyActivityRepository activityRepository,
            UserRepository userRepository
    ) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void recordActivity(String studentId) {

        LocalDate today = LocalDate.now();


        boolean alreadyRecorded =
                activityRepository.existsByStudent_UserIdAndActivityDate(
                        studentId,
                        today
                );


        if (alreadyRecorded) {
            return;
        }


        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found")
                );


        StudentDailyActivity activity =
                new StudentDailyActivity(student, today);


        activityRepository.save(activity);
    }
}