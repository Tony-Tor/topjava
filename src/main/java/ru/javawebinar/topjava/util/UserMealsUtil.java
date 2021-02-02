package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> countOfCaloriesMap = new HashMap<>();

        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        for(UserMeal userMeal: meals) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            Integer countOfCalories = countOfCaloriesMap.get(localDate);

            if(countOfCalories == null){
                atomicBoolean = new AtomicBoolean(true);
                countOfCalories = 0;
            }

            int newCountOfCalories = countOfCalories + userMeal.getCalories();
            countOfCaloriesMap.put(localDate, newCountOfCalories);
            atomicBoolean.set(newCountOfCalories > caloriesPerDay);

            LocalTime localTime = userMeal.getDateTime().toLocalTime();
            if(TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime)) {
                result.add(new UserMealWithExcess(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        atomicBoolean));

            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> countOfCaloriesMap = new HashMap<>();
        Map<LocalDate, AtomicBoolean> atomicBooleanMap = new HashMap<>();

        return meals.stream().map(
                userMeal -> {
                    LocalDate localDate = userMeal.getDateTime().toLocalDate();
                    Integer countOfCalories = countOfCaloriesMap.get(localDate);

                    if(countOfCalories == null){
                        atomicBooleanMap.put(localDate, new AtomicBoolean(true));
                        countOfCalories = 0;
                    }

                    int newCountOfCalories = countOfCalories + userMeal.getCalories();
                    countOfCaloriesMap.put(localDate, newCountOfCalories);

                    AtomicBoolean atomicBoolean = atomicBooleanMap.get(localDate);
                    atomicBoolean.set(newCountOfCalories > caloriesPerDay);

                    return new UserMealWithExcess(
                            userMeal.getDateTime(),
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            atomicBoolean);
                }
        ).filter(userMeal -> {
            LocalTime localTime = userMeal.getDateTime().toLocalTime();
            return TimeUtil.isBetweenHalfOpen(localTime, startTime, endTime);
        }).collect(Collectors.toList());
    }
}
