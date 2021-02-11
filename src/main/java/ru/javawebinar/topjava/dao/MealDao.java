package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao implements IMealDao {

    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @Override
    public Meal add(Meal meal) {
        int maxId = COUNTER.getAndIncrement();
        Meal result = new Meal(maxId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        meals.putIfAbsent(maxId, result);
        return result;
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        if(!meals.containsKey(meal.getId())){
            return null;
        }
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }
}
