package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    User getById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User with id=" + id + " not found"));
    }
}
