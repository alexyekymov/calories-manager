package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.AuthUser;
import dev.overlax.caloriesmanager.service.UserService;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import dev.overlax.caloriesmanager.web.payload.Views;
import jakarta.validation.Valid;
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

    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<UserPayload> get(@AuthenticationPrincipal AuthUser authUser) {
        UserPayload user = service.getById(authUser.id());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/with-meals")
    @JsonView(Views.WithChildren.class)
    public ResponseEntity<UserPayload> getWithMeals(@AuthenticationPrincipal AuthUser authUser) {
        UserPayload user = service.getWithMeals(authUser.id());
        return ResponseEntity.ok(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Public.class)
    public ResponseEntity<UserPayload> register(@RequestBody @Valid UserPayload userReq,
                                                UriComponentsBuilder uriBuilder) {
        UserPayload created = service.prepareAndSave(userReq);
        return ResponseEntity
                .created(uriBuilder
                        .path(REST_URI)
                        .build()
                        .toUri())
                .body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@AuthenticationPrincipal AuthUser authUser,
                                       @RequestBody @Valid UserPayload userReq) {
        service.update(userReq, authUser.id());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthUser authUser) {
        service.delete(authUser.id());
        return ResponseEntity.noContent().build();
    }
}
