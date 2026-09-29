package com.campuscoin.backend.repository;

import com.campuscoin.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByCreatedAtDesc();

    @Query("select case when u.profilePhoto is not null then true else false end from User u where u.userId = :userId")
    boolean hasProfilePhoto(@Param("userId") String userId);
}
