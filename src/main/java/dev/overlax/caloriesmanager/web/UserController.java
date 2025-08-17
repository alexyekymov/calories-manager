package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.service.UserService;
import dev.overlax.caloriesmanager.util.UserUtils;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import dev.overlax.caloriesmanager.web.payload.Views;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserPayload> get() {
        User user = service.getById(UserUtils.TEST_USER_ID);
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
    @JsonView(Views.Public.class)
    public ResponseEntity<Void> update(@RequestBody UserPayload userReq) {
        service.update(userReq, UserUtils.TEST_USER_ID);

        return ResponseEntity.noContent().build();
    }

}
