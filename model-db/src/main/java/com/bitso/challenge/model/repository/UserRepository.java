package com.bitso.challenge.model.repository;

import com.bitso.challenge.entity.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
   @CreatedBy
   Optional<User> findById(long id);
   User save(User user);
}
