package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id) {
        Meal meal = get(id);
        checkOwnUser(meal);
        repository.delete(id);
    }

    public Meal get(int id) {
        return checkOwnUser(checkNotFoundWithId(repository.get(id), id));
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(repository.getAll().stream()
                .filter((meal -> meal.getUserId() == SecurityUtil.authUserId()))
                .collect(Collectors.toList()), SecurityUtil.authUserCaloriesPerDay());
    }

    public void update(Meal meal) {
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

}