package com.burikantushop.burikantushop.service;

import com.burikantushop.burikantushop.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

//    User createUser(User user);
//
//    List<User> getAllUsers();
//
//    Optional<User> getUserById(Long id);
//
//    Optional<User> getUserByEmail(String email);
//
//    User updateUser(Long id, User user);
//
//    void deleteUser(Long id);

    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    User updateUser(Long id, User user);

    void deleteUser(Long id);



}
