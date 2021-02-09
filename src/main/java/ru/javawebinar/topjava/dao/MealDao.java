package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealDao {

    public void addMeal(Meal meal){
        MealsUtil.getMealList().add(meal);
    }

    public void deleteMeal(int id){
        MealsUtil.getMealList().remove(id);
    }

    public void updateMeal(Meal meal){

    }

    public List<Meal> getAllMeals(){
        return MealsUtil.getMealList();
    }

    public Meal getMealById(int id){
        return MealsUtil.getMealList().get(id);
    }
}
