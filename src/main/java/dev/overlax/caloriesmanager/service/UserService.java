package dev.overlax.caloriesmanager.service;

import dev.overlax.caloriesmanager.config.SecurityConfig;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.repository.UserRepository;
import dev.overlax.caloriesmanager.util.exception.NotFoundException;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserPayloadMapper mapper;
    private final MessageService messageService;

    @Cacheable("users.all")
    public List<UserPayload> getAll() {
        List<User> all = repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
        return mapper.toDTOs(all);
    }

    @Cacheable("users.byId")
    public UserPayload getById(int id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    String msg = messageService.getMessage("error.user.id.not-found", id);
                    return new NotFoundException(msg);
                });
    }

    @Cacheable("users.byEmail")
    public UserPayload findByEmailIgnoreCase(String email) {
        return repository.findByEmailIgnoreCase(email)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    String msg = messageService.getMessage("error.user.email.not-found", email);
                    return new NotFoundException(msg);
                });
    }

    @Cacheable("users.withMeals")
    public UserPayload getWithMeals(int id) {
        return repository.findByIdWithMeals(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> {
                    String msg = messageService.getMessage("error.user.id.not-found", id);
                    return new NotFoundException(msg);
                });
    }

    @Transactional
    @Caching(put = {
            @CachePut(cacheNames = "users.byId", key = "#result.id"),
            @CachePut(cacheNames = "users.byEmail", key = "#result.email"),
            @CachePut(cacheNames = "users.withMeals", key = "#result.id")
    })
    @CacheEvict(cacheNames = "users.all", allEntries = true)
    public UserPayload prepareAndSave(UserPayload userReq) {
        User user = mapper.toEntity(userReq);
        user.setPassword(SecurityConfig.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());

        User saved = repository.save(user);
        return mapper.toDTO(saved);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "users.byId", key = "#userId"),
            @CacheEvict(cacheNames = "users.withMeals", key = "#userId"),
            @CacheEvict(cacheNames = "users.byEmail", allEntries = true),
            @CacheEvict(cacheNames = "users.all", allEntries = true)
    })
    public void update(UserPayload userReq, int userId) {
        User user = Optional.of(repository.getReferenceById(userId))
                .orElseThrow(() -> {
                    String msg = messageService.getMessage("error.user.not-found", userId);
                    return new NotFoundException(msg);
                });

        mapper.updateFromDTO(userReq, user);

        if (userReq.password() != null) {
            user.setPassword("{noop}" + userReq.password());
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "users.byId", key = "#id"),
            @CacheEvict(cacheNames = "users.withMeals", key = "#id"),
            @CacheEvict(cacheNames = "users.byEmail", allEntries = true),
            @CacheEvict(cacheNames = "users.all", allEntries = true)
    })
    public void delete(int id) {
        if (repository.deleteUserById(id) == 0) {
            String msg = messageService.getMessage("error.user.id.not-found", id);
            throw new NotFoundException(msg);
        }
    }
}
