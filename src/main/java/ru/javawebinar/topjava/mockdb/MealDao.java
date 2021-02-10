package ru.javawebinar.topjava.mockdb;

import ru.javawebinar.topjava.dao.IMealDao;
import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MealDao implements IMealDao {

    MockDataBase mockDataBase = MockDataBase.getInstance();

    @Override
    public void add(Meal meal) {
        Optional<Integer> optionalMax = mockDataBase.meals.keySet().stream().max(Integer::compare);
        int maxId = 1;
        if(optionalMax.isPresent()){
            maxId = optionalMax.get() + 1;
        }

        mockDataBase.meals.putIfAbsent(maxId, new Meal(maxId, meal.getDateTime(), meal.getDescription(), meal.getCalories()));
    }

    @Override
    public void delete(int id) {
        mockDataBase.meals.remove(id);
    }

    @Override
    public void update(Meal meal) {
        mockDataBase.meals.put(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mockDataBase.meals.values());
    }

    @Override
    public Meal getById(int id) {
        return mockDataBase.meals.get(id);
    }
}
