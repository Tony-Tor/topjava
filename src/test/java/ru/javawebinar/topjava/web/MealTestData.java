package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int OFFSET = 2;
    public static final Meal MEAL1 = new Meal(START_SEQ + OFFSET + 0, LocalDateTime.parse("2020-01-30T10:00:00"), "Завтрак", 500);
    public static final Meal MEAL2 = new Meal(START_SEQ + OFFSET + 1, LocalDateTime.parse("2020-01-30T13:00:00"), "Обед", 1000);
    public static final Meal MEAL3 = new Meal(START_SEQ + OFFSET + 2, LocalDateTime.parse("2020-01-30T20:00:00"), "Ужин", 500);
    public static final Meal MEAL4 = new Meal(START_SEQ + OFFSET + 3, LocalDateTime.parse("2020-01-31T00:00:00"), "Еда на граничное значение", 100);
    public static final Meal MEAL5 = new Meal(START_SEQ + OFFSET + 4, LocalDateTime.parse("2020-01-31T10:00:00"), "Завтрак", 1000);
    public static final Meal MEAL6 = new Meal(START_SEQ + OFFSET + 5, LocalDateTime.parse("2020-01-31T13:00:00"), "Обед", 500);
    public static final Meal MEAL7 = new Meal(START_SEQ + OFFSET + 6, LocalDateTime.parse("2020-01-31T20:00:00"), "Ужин", 410);

    public static final int NOT_FOUND = 100;

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.parse("2021-01-01T00:00:01"), "New Meal", 500);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL6);
        updated.setDateTime(LocalDateTime.parse("2021-01-02T01:00:01"));
        updated.setDescription("new Description");
        updated.setCalories(1000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
