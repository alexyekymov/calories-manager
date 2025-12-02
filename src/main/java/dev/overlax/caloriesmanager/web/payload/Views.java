package dev.overlax.caloriesmanager.web.payload;

/**
 * It's definitely not a good idea to solve the TO problem with {@link com.fasterxml.jackson.annotation.JsonView},
 * but I have to try it at least once.<br/><br/>
 * <p>
 * Use {@code @JsonView(Views.Public.class)} to show in every usual response.<br/>
 * Use {@code @JsonView(Views.WithChildren.class)} to show a child or a list of children or any other relations.<br/>
 * Use none of them if you don't want to show it at all.
 */
public class Views {
    public interface Public {}

    public interface Private extends Public {}

    public interface WithChildren extends Public {}
}
