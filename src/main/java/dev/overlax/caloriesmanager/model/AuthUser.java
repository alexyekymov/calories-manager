package dev.overlax.caloriesmanager.model;

import lombok.Getter;
import org.springframework.lang.NonNull;

public class AuthUser extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.getId();
    }

    @Override
    public String toString() {
        return "AuthUser: " + id() + '[' + user.getEmail() + ']';
    }
}
