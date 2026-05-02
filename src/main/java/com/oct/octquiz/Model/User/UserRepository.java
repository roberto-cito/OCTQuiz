package com.oct.octquiz.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> getAllByRuolo(String ruolo);
    void removeByEmail(String email);   
}

