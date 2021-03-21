package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller

public class JspMealController {

    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);
    @Autowired
    MealService service;

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", MealsUtil.getTos(
                service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay())
        );
        return "meals";
    }

    @RequestMapping(value = "/meals/create", method = RequestMethod.GET)
    public String create(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute(meal);
        return "/mealForm";
    }
    @RequestMapping(value = "/meals/filter", method = RequestMethod.GET)
    public String filtered(@RequestParam String startDate,
                           @RequestParam String endDate,
                           @RequestParam String startTime,
                           @RequestParam String endTime,
                           Model model){

        LocalDate startDatel = parseLocalDate(startDate);
        LocalDate endDatel = parseLocalDate(endDate);
        LocalTime startTimel = parseLocalTime(startTime);
        LocalTime endTimel = parseLocalTime(endTime);

        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDatel, endDatel, userId);
        model.addAttribute(
                "meals",
                MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTimel, endTimel)
        );

        return "/meals";
    }

    @RequestMapping(value = "/meals/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable("id")Integer id, Model model) {
        Meal meal = service.get(id, SecurityUtil.authUserId());
        model.addAttribute(meal);
        return "/mealForm";
    }

    @PostMapping("/meals")
    public String setMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            ValidationUtil.assureIdConsistent(meal, getMealId(request));
            service.update(meal, SecurityUtil.authUserId());
        } else {
            service.create(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id")Integer id) {
        service.delete(id, SecurityUtil.authUserId());

        return "redirect:/meals";
    }

    public int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
