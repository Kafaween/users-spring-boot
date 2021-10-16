package com.example.usersalbums.repositories;

import com.example.usersalbums.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
    Users findUsersByEmail(String email);
    Optional<Users> findUsersByUsername(String userName);
}
