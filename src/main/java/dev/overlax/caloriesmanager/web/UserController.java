package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.AuthUser;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.service.UserService;
import dev.overlax.caloriesmanager.util.UserUtils;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import dev.overlax.caloriesmanager.web.payload.Views;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = UserController.REST_URI, produces = MediaType.APPLICATION_JSON_VALUE)

@RequiredArgsConstructor
public class UserController {
    public static final String REST_URI = "/api/profile";

    private final UserService service;
    private final UserPayloadMapper mapper;

    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<UserPayload> get(@AuthenticationPrincipal AuthUser authUser) {
        User user = service.getById(authUser.id());
        return ResponseEntity.ok(mapper.toDTO(user));
    }

    @GetMapping("/with-meals")
    @JsonView(Views.WithChildren.class)
    public ResponseEntity<UserPayload> getWithMeals(@AuthenticationPrincipal AuthUser authUser) {
        User user = service.getWithMeals(authUser.id());

        return ResponseEntity.ok(mapper.toDTO(user));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<UserPayload> register(@RequestBody UserPayload userReq, UriComponentsBuilder uriBuilder) {
        User user = mapper.toEntity(userReq);
        User created = service.prepareAndSave(user);
        return ResponseEntity
                .created(uriBuilder
                        .path(REST_URI)
                        .build(created.getId()))
                .body(mapper.toDTO(created));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserPayload userReq) {
        service.update(userReq, authUser.id());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthUser authUser) {
        service.delete(authUser.id());
        return ResponseEntity.noContent().build();
    }
}
