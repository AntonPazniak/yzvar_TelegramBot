package com.example.yzvar_telegrambot.repositories;

import com.example.yzvar_telegrambot.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
