package dev.overlax.caloriesmanager.model;

import dev.overlax.caloriesmanager.util.UserUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor()
@Entity
@Table(name = "users")
public class User extends NamedEntity implements HasIdAndEmail {

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Column(name = "registered")
    private Date registered = new Date();

    @Column(name = "calories_per_day")
    private Integer caloriesPerDay = UserUtils.DEFAULT_CALORIES_PER_DAY;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = EnumSet.noneOf(Role.class);

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Meal> meals;

    public User(Integer id, String name, String email, String password, boolean enabled, Date registered,
                Integer caloriesPerDay, Set<Role> roles) {
        super(id, name);
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.registered = registered;
        this.caloriesPerDay = caloriesPerDay;
        this.roles = roles;
    }

    public User(Integer id, String name, String email, String password, int caloriesPerDay) {
        this(id, name, email, password, true, new Date(), caloriesPerDay, Set.of(Role.USER));
    }

    @Override
    public String toString() {
        return "User: " + id + " - [" + email + ']';
    }
}
