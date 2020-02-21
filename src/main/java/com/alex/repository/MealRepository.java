package com.alex.repository;

import com.alex.model.Meal;

import java.util.Collection;

public interface MealRepository {

    Meal save(Meal meal);

    boolean delete(int id);

    Meal get(int id);

    Collection<Meal> getAll();
}
