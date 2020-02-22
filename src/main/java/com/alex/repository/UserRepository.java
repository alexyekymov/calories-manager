package com.alex.repository;

import com.alex.model.User;

import java.util.List;

public interface UserRepository {
    User save(User save);

    boolean delete(int id);

    User get(int id);

    User getByEmail(String email);

    List<User> getAll();
}
