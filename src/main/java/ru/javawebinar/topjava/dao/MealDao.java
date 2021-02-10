package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

public class MealDao implements IMealDao{

    @Override
    public void addMeal(Meal meal){
        MealsUtil.getMealList().add(meal);
    }

    @Override
    public void deleteMeal(int id){
        MealsUtil.getMealList().removeIf(meal -> meal.getId() == id);
    }

    @Override
    public void updateMeal(int id, Meal meal){
        deleteMeal(id);
        addMeal(meal);
    }

    @Override
    public List<Meal> getAllMeals(){
        return MealsUtil.getMealList();
    }

    @Override
    public Meal getMealById(int id){
        for(Meal meal:MealsUtil.getMealList()){
            if(meal.getId() == id) return meal;
        }
        return null;
    }
}
