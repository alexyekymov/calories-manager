package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.config.SecurityConfig;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.UserRepository;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserPayloadMapper mapper;

    @Cacheable("users.byId")
    public User getById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("User with id=" + id + " not found"));
    }

    @Cacheable("users.byEmail")
    public User findByEmailIgnoreCase(String email) {
        return repository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new RuntimeException(String.format("User with '%s' was not found", email)));
    }

    @Cacheable("users.withMeals")
    public User getWithMeals(int id) {
        return repository.findByIdWithMeals(id)
                .orElseThrow(() -> new RuntimeException("User with id=" + id + " not found"));
    }

    @Transactional
    @Caching( put = {
            @CachePut(cacheNames = "users.byId", key = "#result.id"),
            @CachePut(cacheNames = "users.byEmail", key = "#result.email"),
            @CachePut(cacheNames = "users.withMeals", key = "#result.id")
    })
    public User prepareAndSave(User user) {
        user.setPassword(SecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return repository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "users.byId", key = "#userId"),
            @CacheEvict(cacheNames = "users.withMeals", key = "#userId"),
            @CacheEvict(cacheNames = "users.byEmail", allEntries = true)
    })
    public void update(UserPayload userReq, int userId) {
        User user = this.getById(userId); // rewrite during refactoring

        mapper.updateFromDTO(userReq, user);

        if (userReq.password() != null) {
            user.setPassword("{noop}" + userReq.password());
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "users.byId", key = "#result.id"),
            @CacheEvict(cacheNames = "users.withMeals", key = "#result.id"),
            @CacheEvict(cacheNames = "users.byEmail", allEntries = true)
    })
    public void delete(int id) {
        repository.deleteById(id);
    }
}
