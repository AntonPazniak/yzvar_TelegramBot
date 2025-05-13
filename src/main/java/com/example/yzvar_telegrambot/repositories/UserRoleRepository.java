package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.user.UserRole;
import com.example.yzvar_telegrambot.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(UserRoleEnum name);
}

