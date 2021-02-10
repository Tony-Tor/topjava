package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface IMealDao {

    void addMeal(Meal meal);

    void deleteMeal(int id);

    void updateMeal(int id, Meal meal);

    List<Meal> getAllMeals();

    Meal getMealById(int id);

}
