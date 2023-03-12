package com.bitso.challenge.model.db;

import java.util.Optional;
import com.bitso.challenge.entity.User;
import com.bitso.challenge.model.UserModel;
import com.bitso.challenge.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserModelImpl implements UserModel {

  @Autowired
  UserRepository userRepository;

  @Override
  public Optional<User> get(long id) {
    return userRepository.findById(id);
  }

  public void add(User user) {
    userRepository.save(user);
  }
}
