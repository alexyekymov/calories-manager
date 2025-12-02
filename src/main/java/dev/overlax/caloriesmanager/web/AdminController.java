package dev.overlax.caloriesmanager.web;

import com.fasterxml.jackson.annotation.JsonView;
import dev.overlax.caloriesmanager.model.User;
import dev.overlax.caloriesmanager.service.UserService;
import dev.overlax.caloriesmanager.web.mapper.UserPayloadMapper;
import dev.overlax.caloriesmanager.web.payload.UserPayload;
import dev.overlax.caloriesmanager.web.payload.Views;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = AdminController.REST_URI, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminController {

    public static final String REST_URI = "/api/admin/users";

    private final UserService service;
    private final UserPayloadMapper mapper;

    @GetMapping
    @JsonView(Views.Private.class)
    public ResponseEntity<List<UserPayload>> getAll() throws InterruptedException {
        List<UserPayload> dtoList = mapper.toDTOs(service.getAll());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Private.class)
    public ResponseEntity<UserPayload> get(@PathVariable int id) {
        User user = service.getById(id);
        return ResponseEntity.ok(mapper.toDTO(user));
    }

    @GetMapping("/{id}/with-meals")
    @JsonView(Views.WithChildren.class)
    public ResponseEntity<UserPayload> getWithMeals(@PathVariable int id) {
        User user = service.getWithMeals(id);
        return ResponseEntity.ok(mapper.toDTO(user));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Private.class)
    public ResponseEntity<UserPayload> createWithLocation(@RequestBody @Valid UserPayload userReq,
                                                          UriComponentsBuilder uriBuilder) {
        User user = mapper.toEntity(userReq);
        User created = service.prepareAndSave(user);

        return ResponseEntity
                .created(uriBuilder
                        .path(REST_URI + "/{id}")
                        .build(created.getId()))
                .body(mapper.toDTO(created));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable int id,
                                       @RequestBody @Valid UserPayload userReq) {
        service.update(userReq, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
