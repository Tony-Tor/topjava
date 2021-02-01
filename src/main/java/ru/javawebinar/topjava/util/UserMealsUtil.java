package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collector;
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
        List<UserMeal> copyMeal = new ArrayList<>(meals);

        copyMeal.sort(Comparator.comparing(UserMeal::getDateTime));

        List<UserMealWithExcess> result = new ArrayList<>();
        int countCalories = 0;
        LocalDate currentData = null;
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        for(UserMeal userMeal: copyMeal) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();

            if(!localDate.equals(currentData)){
                boolean bool = countCalories > caloriesPerDay;
                atomicBoolean.set(bool);
                atomicBoolean = new AtomicBoolean(true);

                currentData = userMeal.getDateTime().toLocalDate();
                countCalories = userMeal.getCalories();
            } else {
                countCalories += userMeal.getCalories();
            }

            LocalTime localTime = userMeal.getDateTime().toLocalTime();
            if(localTime.isAfter(startTime) && localTime.isBefore(endTime)) {
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
        return meals.stream()
                .collect(Collectors.groupingBy(a -> a.getDateTime().toLocalDate()))
                .entrySet().stream().flatMap((x) -> {
                    int countCaloriesPerDay = x.getValue().stream().mapToInt(UserMeal::getCalories).sum();
                    AtomicBoolean atomicBoolean = new AtomicBoolean(countCaloriesPerDay > caloriesPerDay);
                    return x.getValue().stream().map(y -> new UserMealWithExcess(y.getDateTime(), y.getDescription(),
                            y.getCalories(), atomicBoolean));
                }).filter((x) -> {
                    LocalTime localTime = x.getDateTime().toLocalTime();
                    return localTime.isAfter(startTime) && localTime.isBefore(endTime);
                }).sorted((Comparator.comparing(UserMealWithExcess::getDateTime))).collect(Collectors.toList());
    }
}
