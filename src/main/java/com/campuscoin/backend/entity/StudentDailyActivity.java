package com.campuscoin.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "student_daily_activity",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_activity_date",
                        columnNames = {"student_id", "activity_date"}
                )
        }
)
public class StudentDailyActivity {

    @Id
    @Column(
            name = "id",
            length = 36,
            nullable = false,
            updatable = false
    )
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_activity_student")
    )
    private User student;

    @Column(
            name = "activity_date",
            nullable = false
    )
    private LocalDate activityDate;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    protected StudentDailyActivity() {
    }

    public StudentDailyActivity(User student, LocalDate activityDate) {
        this.student = student;
        this.activityDate = activityDate;
    }

    @PrePersist
    protected void onCreate() {

        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        if (activityDate == null) {
            activityDate = LocalDate.now();
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}