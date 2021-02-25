package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.topjava.web.MealTestData.*;
import static ru.javawebinar.topjava.web.MealTestData.MEAL7;
import static ru.javawebinar.topjava.web.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app-test.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest{

    @Autowired
    MealRepository repository;

    @Test
    public void save() {
        Meal created = repository.save(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(repository.get(newId, USER_ID), newMeal);
    }

    @Test
    public void delete() {
        repository.delete(MEAL7.getId(), USER_ID);
        assertTrue(repository.get(MEAL7.getId(), USER_ID) == null);
    }

    @Test
    public void get() {
        Meal meal = repository.get(MEAL5.getId(), USER_ID);
        assertMatch(meal, MEAL5);
    }

    @Test
    public void getAll() {
        List<Meal> all = repository.getAll(USER_ID);
        assertMatch(all, MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> filtered = repository.getBetweenHalfOpen(
                LocalDateTime.parse("2020-01-30T12:00:00"),
                LocalDateTime.parse("2020-01-30T14:00:00"),
                USER_ID);
        assertMatch(filtered, MEAL2);
    }
}
