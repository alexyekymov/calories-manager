package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.UserRepository;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserPayloadMapper mapper;

    public User getById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User with id=" + id + " not found"));
    }

    @Transactional
    public User prepareAndSave(User user) {
        user.setPassword("{noop}" + user.getPassword());
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }

    @Transactional
    public void update(UserPayload userReq, int userId) {
        User user = this.getById(userId);

        mapper.updateFromDTO(userReq, user);

        if (userReq.password() != null) {
            user.setPassword("{noop}" + userReq.password());
        }
    }
}
