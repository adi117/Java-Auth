package com.example.auth.auth.infrastructure.repositories;

import com.example.auth.auth.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
  Optional<Users> findByEmail(String email);
}
