package ru.javawebinar.topjava.mockdb;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockDataBase {

    private static MockDataBase instance;
    protected Map<Integer, Meal> meals;

    public static MockDataBase getInstance() {
        if (instance == null) {
            instance = new MockDataBase();
            instance.meals = new ConcurrentHashMap<>();

            instance.meals.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            instance.meals.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            instance.meals.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            instance.meals.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            instance.meals.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            instance.meals.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            instance.meals.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        }
        return instance;
    }

}
